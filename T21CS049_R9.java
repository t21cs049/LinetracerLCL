import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class T21CS049_R9 extends Robot {

	private int statesNumber = 12;
	private int actionNumber = 7;
	private int actionTable[] = new int[statesNumber];

	public void initMyRobot() throws IOException {
		double qTable[][] = new double[statesNumber][actionNumber];

		BufferedReader in = new BufferedReader(new FileReader("QTable/map9.txt"));

		String line;
		int x = 0;
		int y = 0;
		while ((line = in.readLine()) != null) {
			String[] s = line.split(" ");
			for (x = 0; x < s.length; x++) {
				qTable[y][x] = Double.parseDouble(s[x]);
			}
			y++;
		}

		in.close();

		for (int s = 0; s < qTable.length; s++) {
			int max = 0;
			for (int i = 1; i < qTable[s].length; i++) {
				if (qTable[s][i] >= qTable[s][max]) {
					max = i;
				}
			}
			actionTable[s] = max;
		}

		System.out.println("/////////////////////////");
		for (int i = 0; i < qTable.length; i++) {
			System.out.println("S " + i + " : " + actionTable[i]);
		}
	}

	public void run() throws InterruptedException {
		try {
			// step 2: 学習したQテーブルの最適政策に基づいて
			// スタート位置からゴール位置まで移動
			/* ロボットを初期位置に戻す */
			init();
			initMyRobot();
			while (true) {
				// 線を見失ったとき用に最後のLIGHTの情報を保持する

				// デバッグ用
//				System.out.println("A:" + getColor(LIGHT_A) + " B:" + getColor(LIGHT_B) + " C:" + getColor(LIGHT_C));

				// 右センサの色に応じて分岐
				doAction(selectAction(judgeState()));

				// 速度調整＆画面描画
				delay();

				// ゴールに到達すれば終了
				if (isOnGoal())
					return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private int selectAction(int state) {
		return actionTable[state];
	}

	// 選択された行動に応じたロボットの座標更新を行う
	private void doAction(int action) {
		// マップの外の座標に更新しないように設定
		if (action == 0)
			rotateRight(10);
		if (action == 1)
			rotateRight(20);
		if (action == 2)
			rotateRight(30);
		if (action == 3)
			rotateLeft(10);
		if (action == 4)
			rotateLeft(20);
		if (action == 5)
			rotateLeft(30);
		if (action == 6)
			;
		forward(1);
	}

	// 光センサーの値の組み合わせを一意の状態に対応付ける為の関数
	private int judgeState() {
		///
		return judgeColor(LIGHT_B) + judgeColor(LIGHT_A) * 3 + judgeColor(LIGHT_C) * 6;

	}

	private int judgeColor(int lightName) {

		if (getColor(lightName) == WHITE)
			return 0;
		if (getColor(lightName) == BLACK)
			return 1;
		if (getColor(lightName) == BLUE)
			return 2;
		if (getColor(lightName) == GREEN)
			return 0;

		System.out.println("Error : Not Exist This Color" + getColor(lightName));
		return 0;

	}

}
