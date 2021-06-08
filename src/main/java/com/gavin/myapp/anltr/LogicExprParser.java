package com.gavin.myapp.anltr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class LogicExprParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, AND = 3, OR = 4, VAR = 5, WS = 6;
    public static final int RULE_stat = 0, RULE_expr = 1;

    private static String[] makeRuleNames() {
        return new String[] { "stat", "expr" };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[] { null, "'('", "')'", "'and'", "'or'" };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[] { null, null, null, "AND", "OR", "VAR", "WS" };
    }

    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override
    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "LogicExpr.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public LogicExprParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class StatContext extends ParserRuleContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode EOF() {
            return getToken(LogicExprParser.EOF, 0);
        }

        public StatContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_stat;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof LogicExprListener) ((LogicExprListener) listener).enterStat(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof LogicExprListener) ((LogicExprListener) listener).exitStat(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof LogicExprVisitor) return ((LogicExprVisitor<? extends T>) visitor).visitStat(
                    this
                ); else return visitor.visitChildren(this);
        }
    }

    public final StatContext stat() throws RecognitionException {
        StatContext _localctx = new StatContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_stat);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(4);
                expr(0);
                setState(5);
                match(EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprContext extends ParserRuleContext {

        public ExprContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expr;
        }

        public ExprContext() {}

        public void copyFrom(ExprContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class OrContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public TerminalNode OR() {
            return getToken(LogicExprParser.OR, 0);
        }

        public OrContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof LogicExprListener) ((LogicExprListener) listener).enterOr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof LogicExprListener) ((LogicExprListener) listener).exitOr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof LogicExprVisitor) return ((LogicExprVisitor<? extends T>) visitor).visitOr(
                    this
                ); else return visitor.visitChildren(this);
        }
    }

    public static class VarContext extends ExprContext {

        public TerminalNode VAR() {
            return getToken(LogicExprParser.VAR, 0);
        }

        public VarContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof LogicExprListener) ((LogicExprListener) listener).enterVar(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof LogicExprListener) ((LogicExprListener) listener).exitVar(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof LogicExprVisitor) return ((LogicExprVisitor<? extends T>) visitor).visitVar(
                    this
                ); else return visitor.visitChildren(this);
        }
    }

    public static class AndContext extends ExprContext {

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public TerminalNode AND() {
            return getToken(LogicExprParser.AND, 0);
        }

        public AndContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof LogicExprListener) ((LogicExprListener) listener).enterAnd(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof LogicExprListener) ((LogicExprListener) listener).exitAnd(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof LogicExprVisitor) return ((LogicExprVisitor<? extends T>) visitor).visitAnd(
                    this
                ); else return visitor.visitChildren(this);
        }
    }

    public static class GroupContext extends ExprContext {

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public GroupContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof LogicExprListener) ((LogicExprListener) listener).enterGroup(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof LogicExprListener) ((LogicExprListener) listener).exitGroup(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof LogicExprVisitor) return ((LogicExprVisitor<? extends T>) visitor).visitGroup(
                    this
                ); else return visitor.visitChildren(this);
        }
    }

    public final ExprContext expr() throws RecognitionException {
        return expr(0);
    }

    private ExprContext expr(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ExprContext _localctx = new ExprContext(_ctx, _parentState);
        ExprContext _prevctx = _localctx;
        int _startState = 2;
        enterRecursionRule(_localctx, 2, RULE_expr, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(13);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case T__0:
                        {
                            _localctx = new GroupContext(_localctx);
                            _ctx = _localctx;
                            _prevctx = _localctx;

                            setState(8);
                            match(T__0);
                            setState(9);
                            expr(0);
                            setState(10);
                            match(T__1);
                        }
                        break;
                    case VAR:
                        {
                            _localctx = new VarContext(_localctx);
                            _ctx = _localctx;
                            _prevctx = _localctx;
                            setState(12);
                            match(VAR);
                        }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
                _ctx.stop = _input.LT(-1);
                setState(23);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            setState(21);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 1, _ctx)) {
                                case 1:
                                    {
                                        _localctx = new AndContext(new ExprContext(_parentctx, _parentState));
                                        pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                        setState(15);
                                        if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                                        setState(16);
                                        match(AND);
                                        setState(17);
                                        expr(5);
                                    }
                                    break;
                                case 2:
                                    {
                                        _localctx = new OrContext(new ExprContext(_parentctx, _parentState));
                                        pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                        setState(18);
                                        if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                                        setState(19);
                                        match(OR);
                                        setState(20);
                                        expr(4);
                                    }
                                    break;
                            }
                        }
                    }
                    setState(25);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 1:
                return expr_sempred((ExprContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean expr_sempred(ExprContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 4);
            case 1:
                return precpred(_ctx, 3);
        }
        return true;
    }

    public static final String _serializedATN =
        "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\b\35\4\2\t\2\4\3" +
        "\t\3\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\5\3\20\n\3\3\3\3\3\3\3\3\3\3" +
        "\3\3\3\7\3\30\n\3\f\3\16\3\33\13\3\3\3\2\3\4\4\2\4\2\2\2\35\2\6\3\2\2" +
        "\2\4\17\3\2\2\2\6\7\5\4\3\2\7\b\7\2\2\3\b\3\3\2\2\2\t\n\b\3\1\2\n\13\7" +
        "\3\2\2\13\f\5\4\3\2\f\r\7\4\2\2\r\20\3\2\2\2\16\20\7\7\2\2\17\t\3\2\2" +
        "\2\17\16\3\2\2\2\20\31\3\2\2\2\21\22\f\6\2\2\22\23\7\5\2\2\23\30\5\4\3" +
        "\7\24\25\f\5\2\2\25\26\7\6\2\2\26\30\5\4\3\6\27\21\3\2\2\2\27\24\3\2\2" +
        "\2\30\33\3\2\2\2\31\27\3\2\2\2\31\32\3\2\2\2\32\5\3\2\2\2\33\31\3\2\2" +
        "\2\5\17\27\31";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
