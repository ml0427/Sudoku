package application;

import service.SudokuSrevice;

public class Sudoku {

	public static void main(String[] args) {

		SudokuSrevice sudokuSrevice = new SudokuSrevice();

		Long startTime = System.currentTimeMillis();
		sudokuSrevice.writeSudokuLs();
		for (int j = 0; j < sudokuSrevice.SUDOKU_SIZE; j++) {
			for (int i = 0; i < sudokuSrevice.SUDOKU_SIZE; i++) {
				// 可選數字
				String optional = sudokuSrevice.checkIsError(j, i);
				if ("".equals(optional)) {
					// System.out.println("<<< 復原處理 >>>");
					if (i > 0) {
						i--;
					} else {
						j--;
						i = sudokuSrevice.SUDOKU_SIZE - 1;
					}
					sudokuSrevice.allRecovery(j, i);
					i--;
					// System.out.println("<<< 復原處理 END >>>");
				} else {
					// 選擇數字
					int randomNb = (int) (Math.random() * optional.length());
					String nb = optional.substring(randomNb, randomNb + 1);
					// 紀錄已使用
					sudokuSrevice.usedSudokuLs(j, i, nb);
					// 目前答案
					sudokuSrevice.answerSudokuLs(j, i, nb);
					// 刪除
					sudokuSrevice.allRemove(j, i, nb);
					// printSudokuAllLs();
				}
			}
		}
		Long stopTime = System.currentTimeMillis();

		// sudokuSrevice.printSudokuAllLs();
		// sudokuSrevice.randomDiggingHoles();
		sudokuSrevice.printSudokuAllLs(stopTime, startTime);
		sudokuSrevice.printCreationHistoryRecord();
	}

}
