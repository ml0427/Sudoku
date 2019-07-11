package sudoku;

public class Sudoku {

	static String[][] sudokuLs = new String[9][9];
	static String[][] answerSudokuLs = new String[9][9];
	static String[][] usedSudokuLs = new String[9][9];
	static String[][] used2SudokuLs = new String[9][9];
	static int sudokuSize = 9;

	public static void main(String[] args) {

		Long StartTime = System.currentTimeMillis();
		writeSudokuLs();
		for (int j = 0; j < sudokuSize; j++) {
			for (int i = 0; i < sudokuSize; i++) {
				// System.out.println("現在位置Y軸：" + j + "，X軸：" + i);
				// 可選數字
				String optional = checkIsError(j, i);
				if ("".equals(optional)) {
					// System.out.println("<<< 復原處理 >>>");
					if (i > 0) {
						i--;
					} else {
						j--;
						i = sudokuSize - 1;
					}
					allRecovery(j, i, answerSudokuLs[j][i]);
					answerSudokuLs[j][i] = null;
					i--;
					// System.out.println("<<< 復原處理 END >>>");
					// printSudokuAllLs();
				} else {
					// 選擇數字
					int randomNb = (int) (Math.random() * optional.length());
					String nb = optional.substring(randomNb, randomNb + 1);
					// 紀錄已使用
					usedSudokuLs(j, i, nb);
					// 目前答案
					answerSudokuLs(j, i, nb);
					// 刪除
					allRemove(j, i, nb);

					// printSudokuAllLs();
				}
			}
		}
		printSudokuAllLs();
		Long StopTime = System.currentTimeMillis();
		String UseTime = (StopTime - StartTime) + "";
		System.out.println("用時：" + UseTime + "毫秒\r\n");

		System.out.println("~~~~~~~~~~~used2SudokuLs~~~~~~~~~~~~~");
		for (int i = 0; i < sudokuSize; i++) {
			for (int j = 0; j < sudokuSize; j++) {
				if (j == 2 || j == 5) {
					System.out.print("(" + used2SudokuLs[i][j] + ")" + " | ");
				} else {
					System.out.print("(" + used2SudokuLs[i][j] + ")");
				}
			}
			System.out.println();
			if (i == 2 || i == 5) {
				System.out.println("-------------------------------");
			}
		}
	}

	/**
	 * Error check。 若此到最後有任一候選字為"空"，或此位置全部都選擇過則<<復原處理>>
	 * 
	 * @param y
	 * @param x
	 * @return 可選數字
	 */
	private static String checkIsError(int y, int x) {

		for (int j = y; j < sudokuSize; j++) {
			for (int i = x; i < sudokuSize; i++) {
				if ("".equals(sudokuLs[j][i])) {
					// System.out.println("發現有空的候選欄");
					return "";
				}
			}
		}
		String a = usedSudokuLs[y][x];
		// System.out.println("已選擇的數字：" + a);
		String b = sudokuLs[y][x];
		if (usedSudokuLs[y][x] != null) {
			for (String str : a.split("")) {
				if (b.contains(str)) {
					b = b.replace((str), "");
				}
			}
		}
		// System.out.println("可選擇的數字：" + b);
		if (sudokuLs[y][x] == null || "".equals(b)) {
			// System.out.println("此選擇都不行");
			usedSudokuLs[y][x] = null;
			return "";
		} else {
			return b;
		}
	}

	/**
	 * 寫入『候選字』
	 */
	private static void writeSudokuLs() {

		for (int i = 0; i < sudokuSize; i++) {
			for (int j = 0; j < sudokuSize; j++) {
				sudokuLs[i][j] = "123456789";
			}
		}
	}

	/**
	 * 紀錄已使用的候選字
	 * 
	 * @param y
	 * @param x
	 * @param nb
	 */
	private static void usedSudokuLs(int y, int x, String nb) {

		if (usedSudokuLs[y][x] == null) {
			usedSudokuLs[y][x] = nb;
		} else {
			if (usedSudokuLs[y][x].indexOf(nb) < 0) {
				usedSudokuLs[y][x] = usedSudokuLs[y][x] + nb;
			}
		}
		if (used2SudokuLs[y][x] == null) {
			used2SudokuLs[y][x] = nb;
		} else {
			used2SudokuLs[y][x] = used2SudokuLs[y][x] + nb;
		}
	}

	/**
	 * 紀錄答案
	 * 
	 * @param y
	 * @param x
	 * @param nb
	 */
	private static void answerSudokuLs(int y, int x, String nb) {

		answerSudokuLs[y][x] = nb;
	}

	/**
	 * XYZ復原
	 * 
	 * @param y
	 * @param x
	 * @param nb
	 */
	private static void allRecovery(int y, int x, String nb) {

		xRecovery(y, x, nb);
		// printSudokuAllLs();
		yRecovery(y, x, nb);
		// printSudokuAllLs();
		zRecovery(y, x, nb);
		// printSudokuAllLs();
	}

	/**
	 * 十字確認
	 * 
	 * @param y
	 * @param x
	 * @param nb
	 * @return
	 */
	private static boolean isValueByCrossCheck(int y, int x, String nb) {

		// 水平確認
		for (int j = 0; j < sudokuSize; j++) {
			if (nb.equals(answerSudokuLs[y][j])) {
				return true;
			}
		}
		// 垂直確認
		for (int i = 0; i < sudokuSize; i++) {
			if (nb.equals(answerSudokuLs[i][x])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * X軸復原
	 * 
	 * @param y
	 * @param x
	 * @param nb
	 */
	private static void xRecovery(int y, int x, String nb) {

		boolean isSame = false;
		for (int j = 0; j < sudokuSize; j++) {
			for (int i = 0; i < sudokuSize; i++) {
				// 指定目前位置
				// System.out.println(">>>" + answerSudokuLs[i][j]);
				if (nb.equals(answerSudokuLs[i][j])) {
					isSame = true;
					break;
				} else if (answerSudokuLs[i][j] == null) {
					break;
				}
			}
			if (!isSame)
				sudokuLs[y][j] = sudokuLs[y][j] + nb;
			isSame = false;
			// }

		}

	}

	/**
	 * Y軸復原
	 * 
	 * @param y
	 * @param x
	 * @param nb
	 */
	private static void yRecovery(int y, int x, String nb) {

		for (int i = y; i < sudokuSize; i++) {
			sudokuLs[i][x] = sudokuLs[i][x] + nb;
		}

	}

	/**
	 * Z區復原
	 * 
	 * @param y
	 * @param x
	 * @param nb
	 */
	private static void zRecovery(int y, int x, String nb) {

		int xStart, xEnd, yEnd;
		if (x <= 2) {
			xStart = 0;
			xEnd = 2;
		} else if (x <= 5) {
			xStart = 3;
			xEnd = 5;
		} else {
			xStart = 6;
			xEnd = 8;
		}
		if (y <= 2) {
			yEnd = 2;
		} else if (y <= 5) {
			yEnd = 5;
		} else {
			yEnd = 8;
		}
		// Z區檢查時不包含已檢查的 X軸和Y軸
		for (int i = y + 1; i <= yEnd; i++) {
			for (int j = xStart; j <= xEnd; j++) {
				if (j != x) {
					// System.out.println(">>>" + sudokuLs[i][j]);
					if (!isValueByCrossCheck(i, j, nb)) {
						sudokuLs[i][j] = sudokuLs[i][j] + nb;
					}
				}
			}
		}
	}

	/**
	 * 刪除XYZ的『候選字』
	 * 
	 * @param y
	 * @param x
	 * @param nb
	 */
	private static void allRemove(int y, int x, String nb) {

		xRemove(y, x, nb);
		yRemove(y, x, nb);
		zRemove(y, x, nb);

	}

	/**
	 * X軸刪除『候選字』
	 * 
	 * @param x
	 * @param y
	 * @param nb
	 */
	private static void xRemove(int y, int x, String nb) {

		for (int j = 0; j < sudokuSize; j++) {
			sudokuLs[y][j] = sudokuLs[y][j].replace(nb, "");
		}

	}

	/**
	 * Y軸刪除『候選字』
	 * 
	 * @param y
	 * @param x
	 * @param nb
	 */
	private static void yRemove(int y, int x, String nb) {

		for (int i = 0; i < sudokuSize; i++) {
			sudokuLs[i][x] = sudokuLs[i][x].replace(nb, "");
		}

	}

	/**
	 * Z區刪除『候選字』
	 * 
	 * @param y
	 * @param x
	 * @param nb
	 */
	private static void zRemove(int y, int x, String nb) {
		int xStart, xEnd, yStart, yEnd;
		if (x <= 2) {
			xStart = 0;
			xEnd = 2;
		} else if (x <= 5) {
			xStart = 3;
			xEnd = 5;
		} else {
			xStart = 6;
			xEnd = 8;
		}

		if (y <= 2) {
			yStart = 0;
			yEnd = 2;
		} else if (y <= 5) {
			yStart = 3;
			yEnd = 5;
		} else {
			yStart = 6;
			yEnd = 8;
		}
		for (int i = yStart; i <= yEnd; i++) {
			for (int j = xStart; j <= xEnd; j++) {
				sudokuLs[i][j] = sudokuLs[i][j].replace(nb, "");
			}
		}

	}

	/**
	 * 印出
	 */
	private static void printSudokuAllLs() {

		// System.out.println("~~~~~~~~~~~~sudokuSize~~~~~~~~~~~~");
		// for (int i = 0; i < sudokuSize; i++) {
		// for (int j = 0; j < sudokuSize; j++) {
		// if (j == 2 || j == 5) {
		// System.out.print(sudokuLs[i][j] + "[]");
		// } else {
		// System.out.print(sudokuLs[i][j] + "|");
		// }
		// }
		// System.out.println();
		// }
		// System.out.println("~~~~~~~~~~~usedSudokuLs~~~~~~~~~~~~~");
		// for (int i = 0; i < sudokuSize; i++) {
		// for (int j = 0; j < sudokuSize; j++) {
		// if (j == 2 || j == 5) {
		// System.out.print("(" + usedSudokuLs[i][j] + ")" + "[]");
		// } else {
		// System.out.print("(" + usedSudokuLs[i][j] + ")");
		// }
		// }
		// System.out.println();
		// }
		System.out.println("~~~~~~~~~~~~answerSudokuLs~~~~~~~~~~~~");
		for (int i = 0; i < sudokuSize; i++) {
			for (int j = 0; j < sudokuSize; j++) {
				if (j == 2 || j == 5) {
					System.out.print(answerSudokuLs[i][j] + " | ");
				} else {
					System.out.print(answerSudokuLs[i][j]);
				}
			}
			System.out.println();
			if (i == 2 || i == 5) {
				System.out.println("---------------");
			}
		}
	}

}

// private static boolean test(int y, int x) {
//
// // 判斷是否到最後
// if (y != sudokuSize) {
// // 候選字表不是空值
// if (sudokuLs[y][x] != null) {
// int randomNb = (int) (Math.random() * sudokuLs[y][x].length());
// String nb = sudokuLs[y][x].substring(randomNb, randomNb + 1);
//
// // 寫入已選擇的表
// answerSudokuLs(y, x, nb);
// // 刪除候選字表
// allRemove(y, x, nb);
// }
// } else {
// return true;
// }
// return false;
// }

// 優先選擇後面沒有的數字
// String priorityNumber = sudokuLs[i][j];
// String b = sudokuLs[i][5] + sudokuLs[i][4] + sudokuLs[i][3];
// String c = sudokuLs[i][8] + sudokuLs[i][7] + sudokuLs[i][6];
// int randomNb;
// String nb;
//
//// 取出最後面的候選字
// for (char str : c.toCharArray()) {
// if (priorityNumber.contains((String.valueOf(str)))) {
// priorityNumber = priorityNumber.replace((String.valueOf(str)), "");
// }
// }
// if (priorityNumber.length() != 0) {
// randomNb = (int) (Math.random() * priorityNumber.length());
// nb = priorityNumber.substring(randomNb, randomNb + 1);
// } else {
// priorityNumber = sudokuLs[i][j];
// for (char str : b.toCharArray()) {
// if (priorityNumber.contains((String.valueOf(str)))) {
// priorityNumber = priorityNumber.replace((String.valueOf(str)), "");
// }
// }
// }
// if (priorityNumber.length() != 0) {
// randomNb = (int) (Math.random() * priorityNumber.length());
// nb = priorityNumber.substring(randomNb, randomNb + 1);
// } else {
// randomNb = (int) (Math.random() * sudokuLs[i][j].length());
// nb = sudokuLs[i][j].substring(randomNb, randomNb + 1);
// }
