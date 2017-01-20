/* MathExpressionParser.java */
/* Generated By:JavaCC: Do not edit this line. MathExpressionParser.java */
package net.hakugyokurou.fds.parser;

import net.hakugyokurou.fds.MathExpression;
import net.hakugyokurou.fds.node.BracketNode;
import net.hakugyokurou.fds.node.FractionNode;
import net.hakugyokurou.fds.node.IEvaluable;
import net.hakugyokurou.fds.node.InvalidExpressionException;
import net.hakugyokurou.fds.node.NegNode;
import net.hakugyokurou.fds.node.OperationNode;
import net.hakugyokurou.fds.node.OperationNode.Operation;
import net.hakugyokurou.fds.node.RationalNode;

import java.io.Reader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class MathExpressionParser implements MathExpressionParserConstants {
    static private int[] jj_la1_0;

    static {
        jj_la1_init_0();
    }

        /* public static ArrayList<MathExpression> parseFile(File file) throws InvalidExpressionException, IOException {
                if(!file.isFile() || !file.exists())
                        throw new IllegalArgumentException("File not exists!");
                BufferedReader reader = null;
                try {
                        reader = new BufferedReader(new FileReader(file));
                        return parse(reader);
                } finally {
                        if(reader != null)
                                try {reader.close();} catch (IOException e) {}
                }
        } */

    final private int[] jj_la1 = new int[7];
    final private JJCalls[] jj_2_rtns = new JJCalls[2];
    final private LookaheadSuccess jj_ls = new LookaheadSuccess();
    /**
     * Generated Token Manager.
     */
    public MathExpressionParserTokenManager token_source;
    /**
     * Current token.
     */
    public Token token;
    /**
     * Next token.
     */
    public Token jj_nt;
    SimpleCharStream jj_input_stream;
    private int jj_ntk;
    private Token jj_scanpos, jj_lastpos;
    private int jj_la;
    private int jj_gen;
    private boolean jj_rescan = false;
    private int jj_gc = 0;
    private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
    private int[] jj_expentry;
    private int jj_kind = -1;
    private int[] jj_lasttokens = new int[100];
    private int jj_endpos;

    /**
     * Constructor with InputStream.
     */
    public MathExpressionParser(java.io.InputStream stream) {
        this(stream, null);
    }

    /**
     * Constructor with InputStream and supplied encoding
     */
    public MathExpressionParser(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source = new MathExpressionParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 7; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /**
     * Constructor.
     */
    public MathExpressionParser(java.io.Reader stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new MathExpressionParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 7; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /**
     * Constructor with generated Token Manager.
     */
    public MathExpressionParser(MathExpressionParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 7; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public static ArrayList<MathExpression> parse(Reader reader) throws InvalidExpressionException {
        ArrayList<MathExpression> list;
        try {
            MathExpressionParser parser = new MathExpressionParser(reader);
            list = parser.parse();
        } catch (TokenMgrError e) {
            throw new InvalidExpressionException("Failed to parse", e);
        } catch (Exception e) {
            throw new InvalidExpressionException("Failed to parse", e);
        }
        for (MathExpression expr : list)
            expr.verify();
        return list;
    }

    public static MathExpression parseLine(Reader reader) throws InvalidExpressionException {
        MathExpression expr;
        try {
            MathExpressionParser parser = new MathExpressionParser(reader);
            expr = parser.parseLine();
        } catch (TokenMgrError e) {
            throw new InvalidExpressionException("Failed to parse", e);
        } catch (Exception e) {
            throw new InvalidExpressionException("Failed to parse", e);
        }
        expr.verify();
        return expr;
    }

    private static OperationNode createOperationNode(int kind, IEvaluable left, IEvaluable right) {
        OperationNode node = null;
        switch (kind) {
            case ADD:
                node = new OperationNode(Operation.ADD);
                break;
            case SUB:
                node = new OperationNode(Operation.SUB);
                break;
            case MUL:
                node = new OperationNode(Operation.MUL);
                break;
            case DIV:
                node = new OperationNode(Operation.DIV);
                break;
        }
        node.setLeft(left);
        node.setRight(right);
        return node;
    }

    private static void jj_la1_init_0() {
        jj_la1_0 = new int[]{0x5720, 0x5720, 0x30, 0xc0, 0xc0, 0x5620, 0x5600,};
    }

    final public ArrayList<MathExpression> parse() throws ParseException {
        ArrayList<MathExpression> list = new ArrayList<MathExpression>();
        MathExpression expr = null;
        label_1:
        while (true) {
            switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
                case SUB:
                case EOL:
                case INTEGER:
                case RATIONAL:
                case 12:
                case 14: {
                    break;
                }
                default:
                    jj_la1[0] = jj_gen;
                    break label_1;
            }
            switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
                case SUB:
                case INTEGER:
                case RATIONAL:
                case 12:
                case 14: {
                    expr = parseLine();
                    if (expr != null) {
                        list.add(expr);
                    }
                    break;
                }
                case EOL: {
                    jj_consume_token(EOL);
                    break;
                }
                default:
                    jj_la1[1] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        jj_consume_token(0);
        {
            return list;
        }
    }

    final public MathExpression parseLine() throws ParseException {
        MathExpression expr = new MathExpression();
        IEvaluable evaluable;
        evaluable = sum();
        if (jj_2_1(2)) {
            jj_consume_token(EOL);
        }
        if (evaluable == null) {
            return null;
        }
        expr.setRoot(evaluable);
        {
            return expr;
        }
    }

    private IEvaluable sum() throws ParseException {
        IEvaluable left, right;
        Token token;
        OperationNode currentNode = null;
        left = mul();
        label_2:
        while (true) {
            if (!jj_2_2(2))
                break label_2;
            switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
                case ADD: {
                    token = jj_consume_token(ADD);
                    break;
                }
                case SUB: {
                    token = jj_consume_token(SUB);
                    break;
                }
                default:
                    jj_la1[2] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            right = mul();
            OperationNode node = createOperationNode(token.kind, left, right);
            if (currentNode != null)
                node.setLeft(currentNode);
            currentNode = node;
        }
        {
            return currentNode == null ? left : currentNode;
        }
    }

    private IEvaluable mul() throws ParseException {
        IEvaluable left, right;
        Token token;
        OperationNode currentNode = null;
        left = unary();
        label_3:
        while (true) {
            switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
                case MUL:
                case DIV: {
                    break;
                }
                default:
                    jj_la1[3] = jj_gen;
                    break label_3;
            }
            switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
                case MUL: {
                    token = jj_consume_token(MUL);
                    break;
                }
                case DIV: {
                    token = jj_consume_token(DIV);
                    break;
                }
                default:
                    jj_la1[4] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            right = unary();
            OperationNode node = createOperationNode(token.kind, left, right);
            if (currentNode != null)
                node.setLeft(currentNode);
            currentNode = node;
        }
        {
            return currentNode == null ? left : currentNode;
        }
    }

    private IEvaluable unary() throws ParseException {
        IEvaluable child = null;
        switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
            case SUB: {
                jj_consume_token(SUB);
                child = element();
                {
                    return new NegNode(child);
                }
            }
            case INTEGER:
            case RATIONAL:
            case 12:
            case 14: {
                child = element();
                {
                    return child;
                }
            }
            default:
                jj_la1[5] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
    }

    private IEvaluable element() throws ParseException {
        Token t1, t2;
        IEvaluable child = null;
        switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
            case INTEGER: {
                t1 = jj_consume_token(INTEGER);
                {
                    return new RationalNode(new BigDecimal(t1.image, OperationNode.mathContext));
                }
            }
            case RATIONAL: {
                t1 = jj_consume_token(RATIONAL);
                {
                    return new RationalNode(new BigDecimal(t1.image, OperationNode.mathContext));
                }
            }
            case 12: {
                jj_consume_token(12);
                t1 = jj_consume_token(INTEGER);
                jj_consume_token(DIV);
                t2 = jj_consume_token(INTEGER);
                jj_consume_token(13);
                int n = Integer.parseInt(t1.image), d = Integer.parseInt(t2.image);
                {
                    return new FractionNode(n, d);
                }
            }
            case 14: {
                jj_consume_token(14);
                child = sum();
                jj_consume_token(15);
                {
                    return new BracketNode(child);
                }
            }
            default:
                jj_la1[6] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
    }

    private boolean jj_2_1(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_1();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(0, xla);
        }
    }

    private boolean jj_2_2(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_2();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(1, xla);
        }
    }

    private boolean jj_3_2() {
        Token xsp;
        xsp = jj_scanpos;
        if (jj_scan_token(4)) {
            jj_scanpos = xsp;
            if (jj_scan_token(5)) return true;
        }
        return jj_3R_4();
    }

    private boolean jj_3R_4() {
        return jj_3R_5();
    }

    private boolean jj_3R_8() {
        Token xsp;
        xsp = jj_scanpos;
        if (jj_3R_9()) {
            jj_scanpos = xsp;
            if (jj_3R_10()) {
                jj_scanpos = xsp;
                if (jj_3R_11()) {
                    jj_scanpos = xsp;
                    if (jj_3R_12()) return true;
                }
            }
        }
        return false;
    }

    private boolean jj_3R_9() {
        return jj_scan_token(INTEGER);
    }

    private boolean jj_3_1() {
        return jj_scan_token(EOL);
    }

    private boolean jj_3R_12() {
        return jj_scan_token(14);
    }

    private boolean jj_3R_7() {
        return jj_3R_8();
    }

    private boolean jj_3R_11() {
        return jj_scan_token(12);
    }

    private boolean jj_3R_10() {
        return jj_scan_token(RATIONAL);
    }

    private boolean jj_3R_5() {
        Token xsp;
        xsp = jj_scanpos;
        if (jj_3R_6()) {
            jj_scanpos = xsp;
            if (jj_3R_7()) return true;
        }
        return false;
    }

    private boolean jj_3R_6() {
        return jj_scan_token(SUB);
    }

    /**
     * Reinitialise.
     */
    public void ReInit(java.io.InputStream stream) {
        ReInit(stream, null);
    }

    /**
     * Reinitialise.
     */
    public void ReInit(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream.ReInit(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 7; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /**
     * Reinitialise.
     */
    public void ReInit(java.io.Reader stream) {
        if (jj_input_stream == null) {
            jj_input_stream = new SimpleCharStream(stream, 1, 1);
        } else {
            jj_input_stream.ReInit(stream, 1, 1);
        }
        if (token_source == null) {
            token_source = new MathExpressionParserTokenManager(jj_input_stream);
        }

        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 7; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /**
     * Reinitialise.
     */
    public void ReInit(MathExpressionParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 7; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    private Token jj_consume_token(int kind) throws ParseException {
        Token oldToken;
        if ((oldToken = token).next != null) token = token.next;
        else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        if (token.kind == kind) {
            jj_gen++;
            if (++jj_gc > 100) {
                jj_gc = 0;
                for (JJCalls jj_2_rtn : jj_2_rtns) {
                    JJCalls c = jj_2_rtn;
                    while (c != null) {
                        if (c.gen < jj_gen) c.first = null;
                        c = c.next;
                    }
                }
            }
            return token;
        }
        token = oldToken;
        jj_kind = kind;
        throw generateParseException();
    }

    private boolean jj_scan_token(int kind) {
        if (jj_scanpos == jj_lastpos) {
            jj_la--;
            if (jj_scanpos.next == null) {
                jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
            } else {
                jj_lastpos = jj_scanpos = jj_scanpos.next;
            }
        } else {
            jj_scanpos = jj_scanpos.next;
        }
        if (jj_rescan) {
            int i = 0;
            Token tok = token;
            while (tok != null && tok != jj_scanpos) {
                i++;
                tok = tok.next;
            }
            if (tok != null) jj_add_error_token(kind, i);
        }
        if (jj_scanpos.kind != kind) return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
        return false;
    }

    /**
     * Get the next Token.
     */
    final public Token getNextToken() {
        if (token.next != null) token = token.next;
        else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    /**
     * Get the specific Token.
     */
    final public Token getToken(int index) {
        Token t = token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) t = t.next;
            else t = t.next = token_source.getNextToken();
        }
        return t;
    }

    private int jj_ntk_f() {
        if ((jj_nt = token.next) == null)
            return (jj_ntk = (token.next = token_source.getNextToken()).kind);
        else
            return (jj_ntk = jj_nt.kind);
    }

    private void jj_add_error_token(int kind, int pos) {
        if (pos >= 100) {
            return;
        }

        if (pos == jj_endpos + 1) {
            jj_lasttokens[jj_endpos++] = kind;
        } else if (jj_endpos != 0) {
            jj_expentry = new int[jj_endpos];

            System.arraycopy(jj_lasttokens, 0, jj_expentry, 0, jj_endpos);

            for (int[] oldentry : jj_expentries) {
                if (oldentry.length == jj_expentry.length) {
                    boolean isMatched = true;

                    for (int i = 0; i < jj_expentry.length; i++) {
                        if (oldentry[i] != jj_expentry[i]) {
                            isMatched = false;
                            break;
                        }

                    }
                    if (isMatched) {
                        jj_expentries.add(jj_expentry);
                        break;
                    }
                }
            }

            if (pos != 0) {
                jj_lasttokens[(jj_endpos = pos) - 1] = kind;
            }
        }
    }

    /**
     * Generate ParseException.
     */
    public ParseException generateParseException() {
        jj_expentries.clear();
        boolean[] la1tokens = new boolean[16];
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 7; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 16; i++) {
            if (la1tokens[i]) {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.add(jj_expentry);
            }
        }
        jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
        int[][] exptokseq = new int[jj_expentries.size()][];
        for (int i = 0; i < jj_expentries.size(); i++) {
            exptokseq[i] = jj_expentries.get(i);
        }
        return new ParseException(token, exptokseq, tokenImage);
    }

    /**
     * Enable tracing.
     */
    final public void enable_tracing() {
    }

    /**
     * Disable tracing.
     */
    final public void disable_tracing() {
    }

    private void jj_rescan_token() {
        jj_rescan = true;
        for (int i = 0; i < 2; i++) {
            try {
                JJCalls p = jj_2_rtns[i];

                do {
                    if (p.gen > jj_gen) {
                        jj_la = p.arg;
                        jj_lastpos = jj_scanpos = p.first;
                        switch (i) {
                            case 0:
                                jj_3_1();
                                break;
                            case 1:
                                jj_3_2();
                                break;
                        }
                    }
                    p = p.next;
                } while (p != null);

            } catch (LookaheadSuccess ignored) {
            }
        }
        jj_rescan = false;
    }

    private void jj_save(int index, int xla) {
        JJCalls p = jj_2_rtns[index];
        while (p.gen > jj_gen) {
            if (p.next == null) {
                p = p.next = new JJCalls();
                break;
            }
            p = p.next;
        }

        p.gen = jj_gen + xla - jj_la;
        p.first = token;
        p.arg = xla;
    }

    @SuppressWarnings("serial")
    static private final class LookaheadSuccess extends java.lang.Error {
    }

    static final class JJCalls {
        int gen;
        Token first;
        int arg;
        JJCalls next;
    }

}
