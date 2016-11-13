/* MathExpressionParserTokenManager.java */
/* Generated By:JavaCC: Do not edit this line. MathExpressionParserTokenManager.java */
package net.hakugyokurou.fds.parser;

/**
 * Token Manager.
 */
@SuppressWarnings("unused")
class MathExpressionParserTokenManager implements MathExpressionParserConstants {

    /**
     * Token literal values.
     */
    public static final String[] jjstrLiteralImages = {
            "", null, null, null, "\53", "\55", "\52", "\57", "\12", null, null, null,
            "\133", "\135", "\50", "\51",};
    /**
     * Lexer state names.
     */
    public static final String[] lexStateNames = {
            "DEFAULT",
    };
    static final int[] jjnextStates = {
            1, 2, 3,
    };
    static final long[] jjtoToken = {
            0xf7f1L,
    };
    static final long[] jjtoSkip = {
            0xeL,
    };
    private final int[] jjrounds = new int[5];
    private final int[] jjstateSet = new int[2 * 5];
    /**
     * Debug output.
     */
    public java.io.PrintStream debugStream = System.out;
    protected SimpleCharStream input_stream;
    protected int curChar;
    int curLexState = 0;
    int defaultLexState = 0;
    int jjnewStateCnt;
    int jjround;
    int jjmatchedPos;
    int jjmatchedKind;

    /**
     * Constructor.
     */
    public MathExpressionParserTokenManager(SimpleCharStream stream) {

        if (SimpleCharStream.staticFlag)
            throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");

        input_stream = stream;
    }

    /**
     * Constructor.
     */
    public MathExpressionParserTokenManager(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    /**
     * Set debug output.
     */
    public void setDebugStream(java.io.PrintStream ds) {
        debugStream = ds;
    }

    private int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch (pos) {
            default:
                return -1;
        }
    }

    private int jjStartNfa_0(int pos, long active0) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private int jjStopAtPos(int pos, int kind) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private int jjMoveStringLiteralDfa0_0() {
        switch (curChar) {
            case 10:
                return jjStopAtPos(0, 8);
            case 40:
                return jjStopAtPos(0, 14);
            case 41:
                return jjStopAtPos(0, 15);
            case 42:
                return jjStopAtPos(0, 6);
            case 43:
                return jjStopAtPos(0, 4);
            case 45:
                return jjStopAtPos(0, 5);
            case 47:
                return jjStopAtPos(0, 7);
            case 91:
                return jjStopAtPos(0, 12);
            case 93:
                return jjStopAtPos(0, 13);
            default:
                return jjMoveNfa_0(0, 0);
        }
    }

    private int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        jjnewStateCnt = 5;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff)
                ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                do {
                    switch (jjstateSet[--i]) {
                        case 0:
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 9)
                                kind = 9;
                        {
                            jjCheckNAddStates(0, 2);
                        }
                        break;
                        case 1:
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 9)
                                kind = 9;
                        {
                            jjCheckNAdd(1);
                        }
                        break;
                        case 2:
                            if ((0x3ff000000000000L & l) != 0L) {
                                jjCheckNAddTwoStates(2, 3);
                            }
                            break;
                        case 3:
                            if (curChar != 46)
                                break;
                            kind = 10;
                        {
                            jjCheckNAdd(4);
                        }
                        break;
                        case 4:
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 10)
                                kind = 10;
                        {
                            jjCheckNAdd(4);
                        }
                        break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                do {
                    switch (jjstateSet[--i]) {
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                do {
                    switch (jjstateSet[--i]) {
                        default:
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != 0x7fffffff) {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 5 - (jjnewStateCnt = startsAt)))
                return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    protected Token jjFillToken() {
        final Token t;
        final String curTokenImage;
        final int beginLine;
        final int endLine;
        final int beginColumn;
        final int endColumn;
        String im = jjstrLiteralImages[jjmatchedKind];
        curTokenImage = (im == null) ? input_stream.GetImage() : im;
        beginLine = input_stream.getBeginLine();
        beginColumn = input_stream.getBeginColumn();
        endLine = input_stream.getEndLine();
        endColumn = input_stream.getEndColumn();
        t = Token.newToken(jjmatchedKind, curTokenImage);

        t.beginLine = beginLine;
        t.endLine = endLine;
        t.beginColumn = beginColumn;
        t.endColumn = endColumn;

        return t;
    }

    /**
     * Get the next Token.
     */
    public Token getNextToken() {
        Token matchedToken;
        int curPos = 0;

        EOFLoop:
        for (; ; ) {
            try {
                curChar = input_stream.BeginToken();
            } catch (Exception e) {
                jjmatchedKind = 0;
                jjmatchedPos = -1;
                matchedToken = jjFillToken();
                return matchedToken;
            }

            try {
                input_stream.backup(0);
                while (curChar <= 32 && (0x100002200L & (1L << curChar)) != 0L)
                    curChar = input_stream.BeginToken();
            } catch (java.io.IOException e1) {
                continue EOFLoop;
            }
            jjmatchedKind = 0x7fffffff;
            jjmatchedPos = 0;
            curPos = jjMoveStringLiteralDfa0_0();
            if (jjmatchedKind != 0x7fffffff) {
                if (jjmatchedPos + 1 < curPos)
                    input_stream.backup(curPos - jjmatchedPos - 1);
                if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                    matchedToken = jjFillToken();
                    return matchedToken;
                } else {
                    continue EOFLoop;
                }
            }
            int error_line = input_stream.getEndLine();
            int error_column = input_stream.getEndColumn();
            String error_after = null;
            boolean EOFSeen = false;
            try {
                input_stream.readChar();
                input_stream.backup(1);
            } catch (java.io.IOException e1) {
                EOFSeen = true;
                error_after = curPos <= 1 ? "" : input_stream.GetImage();
                if (curChar == '\n' || curChar == '\r') {
                    error_line++;
                    error_column = 0;
                } else
                    error_column++;
            }
            if (!EOFSeen) {
                input_stream.backup(1);
                error_after = curPos <= 1 ? "" : input_stream.GetImage();
            }
            throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
        }
    }

    private void jjCheckNAdd(int state) {
        if (jjrounds[state] != jjround) {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private void jjAddStates(int start, int end) {
        do {
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        } while (start++ != end);
    }

    private void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private void jjCheckNAddStates(int start, int end) {
        do {
            jjCheckNAdd(jjnextStates[start]);
        } while (start++ != end);
    }

    /**
     * Reinitialise parser.
     */
    public void ReInit(SimpleCharStream stream) {

        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private void ReInitRounds() {
        int i;
        jjround = 0x80000001;
        for (i = 5; i-- > 0; )
            jjrounds[i] = 0x80000000;
    }

    /**
     * Reinitialise parser.
     */
    public void ReInit(SimpleCharStream stream, int lexState) {

        ReInit(stream);
        SwitchTo(lexState);
    }

    /**
     * Switch to specified lex state.
     */
    public void SwitchTo(int lexState) {
        if (lexState >= 1 || lexState < 0)
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
        else
            curLexState = lexState;
    }
}
