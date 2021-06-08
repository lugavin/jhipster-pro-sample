//LogicExpr.g4文件

grammar LogicExpr;

stat:
    expr EOF
    ;

expr: expr AND expr             # and
    | expr OR expr              # or
    | '(' expr ')'              # group
    | VAR                       # var
    ;

AND: 'and' ;
OR: 'or' ;
VAR: [a-zA-Z0-9_]+ ;
WS: [ \t\r\n]+ -> skip ;
