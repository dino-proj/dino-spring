grammar LogFields;

options {
	language = Java;
}

statement
:
	LCURLY
	(
		expression ';'
	)+ RCURLY EOF
;

expression
:
	LPAREN expression RPAREN # ExprWithParen
	| constant # ExprConst
	| callClause # ExprCallClause
	| map # ExprMap
	| left = expression op = OP_JOIN right = expression # ExprOpBinary
	| left = expression op = OP_QUESTION right = expression # ExprOpBinary
	| left = expression op = OP_OR right = expression # ExprOpBinary
;

map
:
	LSQUARE keyvalue
	(
		COMMA keyvalue
	)?
	(
		COMMA defaultkeyvalue
	)? RSQUARE
;

keyvalue
:
	key = constant ':' value = expression
;

defaultkeyvalue
:
	key = '_' ':' value = expression
;

constant
:
	StringLiteral
	| IntegerLiteral
;

callClause
:
	functionName LPAREN paramList? RPAREN
;

functionName
:
	Identifier
;

paramList
:
	paramValues += param
	(
		COMMA paramValues += param
	)*
;

param
:
	StringLiteral
;

COMMA
:
	','
;

LPAREN
:
	'('
;

RPAREN
:
	')'
;

LSQUARE
:
	'['
;

RSQUARE
:
	']'
;

LCURLY
:
	'{'
;

RCURLY
:
	'}'
;

OP_OR
:
	'OR'
;

OP_JOIN
:
	'+'
;

OP_QUESTION
:
	'?'
;

Identifier
:
	Letter
	(
		Letter
		| Digit
		| '_'
	)*
;

StringLiteral
:
	'"' DoubleStringCharacter* '"'
	| '\'' SingleStringCharacter* '\''
;

IntegerLiteral
:
	'0'
	| Sign? NonZeroDigit Digit*
;

fragment
DoubleStringCharacter
:
	~( '"' | '\\' | '\n' | '\r' | '\u2028' | '\u2029' )
	| '\\' EscapeSequence
;

fragment
SingleStringCharacter
:
	~( '\'' | '\\' | '\n' | '\r' | '\u2028' | '\u2029' )
	| '\\' EscapeSequence
;

fragment
Digit
:
	[0-9]
;

fragment
NonZeroDigit
:
	[1-9]
;

fragment
Sign
:
	[-]
;

fragment
EscapeSequence
:
	CharacterEscapeSequence
	| '0'
	| HexEscapeSequence
	| UnicodeEscapeSequence
;

fragment
CharacterEscapeSequence
:
	SingleEscapeCharacter
	| NonEscapeCharacter
;

fragment
NonEscapeCharacter
:
	~( '\'' | '"' | '\\' | 'b' | 'f' | 'n' | 'r' | 't' | 'v' | [0-9] | 'x' | 'u'
	| '\n' | '\r' | '\u2028' | '\u2029' )
;

fragment
SingleEscapeCharacter
:
	'\''
	| '"'
	| '\\'
	| 'b'
	| 'f'
	| 'n'
	| 'r'
	| 't'
	| 'v'
;

fragment
HexEscapeSequence
:
	'x' HexDigit HexDigit
;

fragment
UnicodeEscapeSequence
:
	'u' HexDigit HexDigit HexDigit HexDigit
;

fragment
Letter
:
	(
		'a' .. 'z'
		| 'A' .. 'Z'
	)
;

fragment
HexDigit
:
	(
		'0' .. '9'
		| 'a' .. 'f'
		| 'A' .. 'F'
	)
;

fragment
LT
:
	'\n' // Line feed.

	| '\r' // Carriage return.

	| '\u2028' // Line separator.

	| '\u2029' // Paragraph separator.

;

//
// Whitespace and comments
//

WS
:
	[ \t\r\n\u000C]+ -> skip
;

COMMENT
:
	'/*' .*? '*/' -> skip
;

LINE_COMMENT
:
	'//' ~[\r\n]* -> skip
;