package net.hakugyokurou.fds.node;

public class RationalNode implements IEvaluable {

    private final double value;

    public RationalNode(double value) {
        this.value = value;
    }

    @Override
    public double eval() {
        return value;
    }

    @Override
    public String toString() {
        long lv = (long) value;
        return value == lv ? "" + lv : "" + value;
    }

    @Override
    public void verify() {
        /*
		　　　　　　　　　　　＿＿＿＿/　 　 　 　 l
		　　　    　　　　 x≪圭圭圭圭ﾐ/　　 　 　     ト､
		至至至至至ZZzzx∠ 　 ｀`≪圭ミ{　　　　 　　 }ﾐ会x
		圭圭圭圭圭圭圭圭至≫x､　　`≪l　　　　  　　l圭圭ﾄ、
		ﾐ圭圭圭圭圭癶圭圭圭圭ﾐ会x､　 }　　　　　 　j圭圭圭Y＿＿
		圭圭圭圭㌢　     ／`≪圭圭ミ会x/　　　　　    /／￣￣　　　
		圭l圭圭ｱ        ／　　　/ `≪ﾐ圭圭Y　　〈:}　∥　 　 　 　 　
		`寸圭ﾐ/　　 /　　／∥　　　 `≪ミ{　　  乂l／ ｝＿　 　 　 　 How could it fail?
		　　  /　　 /　　/　 ＿　　　 / 　＼　　    ノ＿/::::ヽ　　　　
		　　/　/  / 　/　 7⌒Y\_　  /　-＝-ミ寸至圭{　ヽ:::}　　　／
		　   ! / l∥ 　 l　 /l l　\ |   / { /　｜`寸ﾐ犾　　 ￣  ／
		    ｛ /  l { 　{　{ ,x云从  |　 l　/r‐{- 　 l　}　ﾄ圭会x　 　 /会x_
		　   乂　 l 从 　 气     (ん心 \　l/ 芹云ﾐx,,l/j   /　`≪圭会z价圭会x
		　　　　　い　＼ 人 ｛::♡:｝＼{　  ん:♡ﾊY/　　  /ヽ l`≪圭圭圭圭
		　　 　 　 ヽlヽ　ﾄ　   乂ン　　} 　  弋:::ﾉﾉj　   /　 ﾊ}　ﾊ`トミ圭圭
		　　　　　 　 　)ﾉ八　,,,,　 '  　　　 　 ｀~~  /　/　 / ﾉ / ﾉ　＼￣￣
		　　　　　　　　/　 l、　　    　　　　　'''　/ ／　 ノ　／　 　 ヽ Y　　
		　　　　　　 　 {　　l ＼　　`ｰ-‐'"’　　 ／｝／　{ {　　乂　　l l　　
		　　　　 　 　  「ヽ　 い {＼　　 　 　 　  / //    ＿ j人ヽ　　＼ } }＼
		　　      r‐ ､　   l }＼乂　 l l ｰ---ｬ''" / /　    ／／:j　 Y ＼　ﾉ  人　
		　　　    ＼ ＼　｜ l　 )人　l从 ＞- ｝  / /   ／／:::/　　} }～---､＼
		　　　　    ＼ ＼ l l　　 八 l:::f‐く  ﾐ/ ｛  　／::::/ 　 ノノ＿／ ﾉヽ
		　　　　　　ヽ＿＿＞く 〔`ﾄ/⌒           ＼　　　 /::::::/　／ ﾉ ／ ⌒＼⌒)
		　　　　　　 }　--く_ノ {r) j::ヽﾉ　 　 ﾉ   /:::::/／　厂/　  ヽ^〉
		　　　　　　〈 ーｧ }(　  })　l:::/　　　　 /::::::〈(　 ／ /　 　 l
		　　　 　 　     ∧ (ノ　   ノ     Y ／{　　　　∧::::/　 厂 ノ　　　　 }
		　　　 　    「￣∧　　  ／ し　        Y　　　/ ∧:/^Yﾉ ／　　　　/　 j
		　　　　　   l　　 / ／　　　　  　l 　 　  lﾆ〕∨｀厂　}　　  ／ 　 j
		　　　 　      l⌒) / /　　　　　 ／l　　   l⌒Y⌒　     ∥　　／　  　/
		　　 　        癶　 / ∥　　 　   /（　 l　　   l　/}　 {/　／　 　　/
		　　       　/　　/ {　　　  ｛⌒ヽ   l 　 　   ∨ﾉ　 l /／　　　　 /
		*/
    }
}
