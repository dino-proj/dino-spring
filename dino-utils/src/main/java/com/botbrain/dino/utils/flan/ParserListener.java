package com.botbrain.dino.utils.flan;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import com.botbrain.dino.utils.flan.LogFieldsParser.CallClauseContext;
import com.botbrain.dino.utils.flan.LogFieldsParser.ConstantContext;
import com.botbrain.dino.utils.flan.LogFieldsParser.DefaultkeyvalueContext;
import com.botbrain.dino.utils.flan.LogFieldsParser.ExprConstContext;
import com.botbrain.dino.utils.flan.LogFieldsParser.ExprMapContext;
import com.botbrain.dino.utils.flan.LogFieldsParser.ExprOpBinaryContext;
import com.botbrain.dino.utils.flan.LogFieldsParser.ExprWithParenContext;
import com.botbrain.dino.utils.flan.LogFieldsParser.KeyvalueContext;
import com.botbrain.dino.utils.flan.LogFieldsParser.ParamContext;
import com.botbrain.dino.utils.flan.LogFieldsParser.StatementContext;
import com.botbrain.dino.utils.flan.function.Function;
import com.botbrain.dino.utils.flan.function.FunctionFactory;
import com.botbrain.dino.utils.flan.function.Oper;
import com.botbrain.dino.utils.flan.function.imp.FNConst;
import com.botbrain.dino.utils.flan.function.imp.StructMap;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ParserListener extends LogFieldsBaseListener {
	private List<Function> expr = new ArrayList<>();
	private boolean debug = false;
	private Deque<List<Function>> buildStack = new ArrayDeque<>();
	private final FunctionFactory fnFactory;

	public ParserListener(FunctionFactory fnFactory) {
		this.fnFactory = fnFactory;
	}

	public ParserListener(FunctionFactory fnFactory, boolean debug) {
		this.fnFactory = fnFactory;
		this.debug = debug;
	}

	@Override
	public void enterStatement(StatementContext ctx) {
		buildStack.push(expr);
		super.enterStatement(ctx);
	}

	@Override
	public void exitStatement(StatementContext ctx) {
		buildStack.pop();
		super.exitStatement(ctx);
	}

	@Override
	public void enterExprWithParen(ExprWithParenContext ctx) {
		debugInfo("exprParen");
		buildStack.push(new ArrayList<>());
		super.enterExprWithParen(ctx);
	}

	@Override
	public void exitExprWithParen(ExprWithParenContext ctx) {
		List<Function> ex = buildStack.pop();
		if (ex.size() != 1) {
			wrapBuildException(ctx, "'('&')' miss matched");
		}
		buildStack.peek().add(ex.remove(0));
		super.exitExprWithParen(ctx);
	}

	@Override
	public void enterExprMap(ExprMapContext ctx) {
		debugInfo("exprMap");
		buildStack.push(new ArrayList<>());
		super.enterExprMap(ctx);
	}

	@Override
	public void enterKeyvalue(KeyvalueContext ctx) {
		String keyValue = constValue(ctx.key);
		Class<? extends Function> fCls = fnFactory.getFunction(FunctionFactory.FUNC_CONST);
		try {
			addFunction(newAndSetupFunction(FunctionFactory.FUNC_CONST, fCls, keyValue));
		} catch (BuildException e) {
			throw wrapBuildException(ctx, e);
		}
		super.enterKeyvalue(ctx);
	}

	@Override
	public void enterDefaultkeyvalue(DefaultkeyvalueContext ctx) {
		addFunction(null);
		super.exitDefaultkeyvalue(ctx);
	}

	@Override
	public void exitExprMap(ExprMapContext ctx) {
		List<Function> ex = buildStack.pop();
		if (ex.size() % 2 != 0) {
			wrapBuildException(ctx, "map not valid");
		}
		StructMap map = new StructMap();

		for (int i = 0; i < ex.size(); i += 2) {
			FNConst key = (FNConst) ex.get(i);
			Function v = ex.get(i + 1);
			if (key == null) {
				map.setDefault(v);
			} else {
				map.addCase(key.value(), v);
			}
		}
		buildStack.peek().add(map);
		super.exitExprMap(ctx);
	}

	@Override
	public void exitExprOpBinary(ExprOpBinaryContext ctx) {
		String opName = ctx.op.getText().toUpperCase();
		List<Function> ex = buildStack.peek();
		if (ex.size() < 2) {
			wrapBuildException(ctx, "opper[" + opName + "] miss matched");
		}

		Class<? extends Oper> opCls = fnFactory.getOper(opName);
		if (opCls == null) {
			throw wrapBuildException(ctx, "opper[" + opName + "] not supported");
		}

		try {
			Oper op = newAndSetupOper(opName, opCls, ex.remove(ex.size() - 2), ex.remove(ex.size() - 1));
			ex.add(op);
		} catch (BuildException e) {
			throw wrapBuildException(ctx, e);
		}

		super.exitExprOpBinary(ctx);
	}

	@Override
	public void enterCallClause(CallClauseContext ctx) {
		String functionName = ctx.functionName().getText();
		List<String> functionParams = new ArrayList<String>();
		if (ctx.paramList() != null) {
			for (ParamContext param : ctx.paramList().paramValues) {
				functionParams.add(toStr(param.StringLiteral()));
			}
		}
		debugInfo("build func:" + functionName + "(" + Arrays.toString(functionParams.toArray()) + ")");
		Class<? extends Function> fCls = fnFactory.getFunction(functionName.toLowerCase());
		if (fCls == null) {
			throw wrapBuildException(ctx, String.format("function[%s] not found", functionName));
		}

		try {
			// no param call.
			if (ctx.paramList() == null) {
				addFunction(newAndSetupFunction(functionName, fCls));
			} else {// multi param call
				addFunction(newAndSetupFunction(functionName, fCls, functionParams.toArray(new String[functionParams.size()])));
			}
		} catch (BuildException e) {
			throw wrapBuildException(ctx, e);
		}
		super.exitCallClause(ctx);
	}

	@Override
	public void enterExprConst(ExprConstContext ctx) {
		String constcommon = constValue(ctx.constant());

		// Constant Expression
		debugInfo("build const:" + constcommon);
		Class<? extends Function> fCls = fnFactory.getFunction(FunctionFactory.FUNC_CONST);
		try {
			addFunction(newAndSetupFunction(FunctionFactory.FUNC_CONST, fCls, constcommon));
		} catch (BuildException e) {
			throw wrapBuildException(ctx, e);
		}
		super.exitExprConst(ctx);
	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		if (debug) {
			debugInfo(StringUtils.repeat('>', ctx.depth()) + ":" + ctx.getClass().getSimpleName() + "=" + ctx.getText());
		}
		super.enterEveryRule(ctx);
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		if (debug) {
			debugInfo(StringUtils.repeat('<', ctx.depth()) + ":" + ctx.getClass().getSimpleName() + "=" + ctx.getText());
		}
		super.exitEveryRule(ctx);
	}

	public Function[] getResult() {
		return expr.toArray(new Function[expr.size()]);
	}

	private void addFunction(Function f) {
		buildStack.peek().add(f);
	}

	private final void debugInfo(String msg) {
		if (log.isDebugEnabled()) {
			log.debug(msg);
		}
	}

	private RuntimeException wrapBuildException(ParserRuleContext ctx, String msg) {
		return new BuildException(msg + String.format(" @line[%s] pos[%s,%s], text:[%s]", ctx.getStart().getLine(),
				ctx.getStart().getCharPositionInLine(), ctx.getStop().getCharPositionInLine(), ctx.getText()));
	}

	private RuntimeException wrapBuildException(ParserRuleContext ctx, BuildException e) {
		return wrapBuildException(ctx, e.getMessage());
	}

	private final String constValue(ConstantContext constantContext) {
		String constcommon = null;
		if (constantContext.IntegerLiteral() != null) {
			constcommon = constantContext.IntegerLiteral().getText();
		} else {
			constcommon = toStr(constantContext.StringLiteral());
		}
		return constcommon;
	}

	private final String toStr(TerminalNode astNode) {
		String str = astNode.getText();
		str = str.substring(1, str.length() - 1);
		return StringEscapeUtils.unescapeJava(str);
	}

	private Function newAndSetupFunction(String funName, Class<? extends Function> fCls, String... params)
			throws BuildException {
		Function f;
		try {
			f = fCls.getDeclaredConstructor().newInstance();
			f.setup(params);
			return f;
		} catch (InstantiationException e) {
			// impossible kill it
			throw new BuildException(e);
		} catch (Exception e) {
			throw new BuildException(String.format("Function[%s] setup faild:(%s)", funName, e.getMessage()), e);
		}
	}

	private Oper newAndSetupOper(String opName, Class<? extends Oper> opCls, Function... fns) throws BuildException {
		try {
			Oper op = opCls.getDeclaredConstructor().newInstance();
			for (Function function : fns) {
				op.addTail(function);
			}
			return op;
		} catch (InstantiationException e) {
			// impossible kill it
			throw new BuildException(e);
		} catch (Exception e) {
			// impossible kill it
			throw new BuildException(String.format("Oper[%s] setup faild:(%s)", opName, e.getMessage()), e);
		}
	}
}
