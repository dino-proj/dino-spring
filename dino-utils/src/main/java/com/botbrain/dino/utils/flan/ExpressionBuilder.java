package com.botbrain.dino.utils.flan;

import com.botbrain.dino.utils.flan.LogFieldsParser.StatementContext;
import com.botbrain.dino.utils.flan.function.Function;
import com.botbrain.dino.utils.flan.function.FunctionFactory;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * 将解析好的语法树转换成一个可执行的函数数组。
 * @author tuuboo
 *
 */
public class ExpressionBuilder {

	/**
	 * 将语法树转换成Function数组
	 * @param rootNode
	 * @return
	 * @throws BuildException 
	 */
	public Function[] build(String expression, FunctionFactory factory) throws BuildException {
		Lexer lexer = new LogFieldsLexer(CharStreams.fromString(expression.toUpperCase()));
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		LogFieldsParser parser = new LogFieldsParser(tokens);
		parser.setBuildParseTree(true);
		parser.addErrorListener(new BaseErrorListener() {

			@Override
			public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
					String msg, RecognitionException e) {
				throw new BuildException("line " + line + ":" + charPositionInLine + " " + msg);
			}

		});
		StatementContext context = parser.statement();

		ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
		ParserListener pl = new ParserListener(factory);
		walker.walk(pl, context);

		return pl.getResult();
	}

}
