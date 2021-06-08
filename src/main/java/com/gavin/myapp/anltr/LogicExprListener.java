package com.gavin.myapp.anltr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LogicExprParser}.
 */
public interface LogicExprListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link LogicExprParser#stat}.
     * @param ctx the parse tree
     */
    void enterStat(LogicExprParser.StatContext ctx);
    /**
     * Exit a parse tree produced by {@link LogicExprParser#stat}.
     * @param ctx the parse tree
     */
    void exitStat(LogicExprParser.StatContext ctx);
    /**
     * Enter a parse tree produced by the {@code or}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     */
    void enterOr(LogicExprParser.OrContext ctx);
    /**
     * Exit a parse tree produced by the {@code or}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     */
    void exitOr(LogicExprParser.OrContext ctx);
    /**
     * Enter a parse tree produced by the {@code var}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     */
    void enterVar(LogicExprParser.VarContext ctx);
    /**
     * Exit a parse tree produced by the {@code var}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     */
    void exitVar(LogicExprParser.VarContext ctx);
    /**
     * Enter a parse tree produced by the {@code and}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     */
    void enterAnd(LogicExprParser.AndContext ctx);
    /**
     * Exit a parse tree produced by the {@code and}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     */
    void exitAnd(LogicExprParser.AndContext ctx);
    /**
     * Enter a parse tree produced by the {@code group}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     */
    void enterGroup(LogicExprParser.GroupContext ctx);
    /**
     * Exit a parse tree produced by the {@code group}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     */
    void exitGroup(LogicExprParser.GroupContext ctx);
}
