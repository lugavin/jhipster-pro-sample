package com.gavin.myapp.anltr;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LogicExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LogicExprVisitor<T> extends ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by {@link LogicExprParser#stat}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStat(LogicExprParser.StatContext ctx);
    /**
     * Visit a parse tree produced by the {@code or}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOr(LogicExprParser.OrContext ctx);
    /**
     * Visit a parse tree produced by the {@code var}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitVar(LogicExprParser.VarContext ctx);
    /**
     * Visit a parse tree produced by the {@code and}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAnd(LogicExprParser.AndContext ctx);
    /**
     * Visit a parse tree produced by the {@code group}
     * labeled alternative in {@link LogicExprParser#expr}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitGroup(LogicExprParser.GroupContext ctx);
}
