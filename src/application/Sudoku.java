package application;

import service.SudokuSrevice;

public class Sudoku {

	public static void main(String[] args) {
		
		Long StartTime = System.currentTimeMillis();
		SudokuSrevice.writeSudokuLs();
		for (int j = 0; j < SudokuSrevice.SUDOKU_SIZE; j++) {
			for (int i = 0; i < SudokuSrevice.SUDOKU_SIZE; i++) {
				// System.out.println("現在位置Y軸：" + j + "，X軸：" + i);
				// 可選數字
				String optional = SudokuSrevice.checkIsError(j, i);
				if ("".equals(optional)) {
					// System.out.println("<<< 復原處理 >>>");
					if (i > 0) {
						i--;
					} else {
						j--;
						i = SudokuSrevice.SUDOKU_SIZE - 1;
					}
					SudokuSrevice.allRecovery(j, i);
					i--;
					// System.out.println("<<< 復原處理 END >>>");
					// printSudokuAllLs();
				} else {
					// 選擇數字
					int randomNb = (int) (Math.random() * optional.length());
					String nb = optional.substring(randomNb, randomNb + 1);
					// 紀錄已使用
					SudokuSrevice.usedSudokuLs(j, i, nb);
					// 目前答案
					SudokuSrevice.answerSudokuLs(j, i, nb);
					// 刪除
					SudokuSrevice.allRemove(j, i, nb);
					// printSudokuAllLs();
				}
			}
		}
		SudokuSrevice.printSudokuAllLs();
		Long StopTime = System.currentTimeMillis();

		SudokuSrevice.printCreationHistoryRecord(StopTime, StartTime);

	}

}
