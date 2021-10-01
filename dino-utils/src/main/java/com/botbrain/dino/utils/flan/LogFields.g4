// Copyright 2021 dinospring.cn
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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