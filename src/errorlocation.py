import os
import cv2
import csv

# シンボル座標
symbols = [
    [(26, 26), (26, 25), (25, 26), (25, 25), (24, 26), (24, 25), (23, 26), (23, 25)],
    [(22, 26), (22, 25), (21, 26), (21, 25), (20, 26), (20, 25), (19, 26), (19, 25)],
    [(18, 26), (18, 25), (17, 26), (17, 25), (16, 26), (16, 25), (15, 26), (15, 25)],
    [(14, 26), (14, 25), (13, 26), (13, 25), (12, 26), (12, 25), (11, 26), (11, 25)],
    [(11, 24), (11, 23), (12, 24), (12, 23), (13, 24), (13, 23), (14, 24), (14, 23)],
    [(15, 24), (15, 23), (16, 24), (16, 23), (17, 24), (17, 23), (18, 24), (18, 23)],
    [(19, 24), (19, 23), (20, 24), (20, 23), (21, 24), (21, 23), (22, 24), (22, 23)],
    [(23, 24), (23, 23), (24, 24), (24, 23), (25, 24), (25, 23), (26, 24), (26, 23)],
    [(26, 22), (26, 21), (25, 22), (25, 21), (24, 22), (24, 21), (23, 22), (23, 21)],
    [(17, 22), (17, 21), (16, 22), (16, 21), (15, 22), (15, 21), (14, 22), (14, 21)],
    [(13, 22), (13, 21), (12, 22), (12, 21), (11, 22), (11, 21), (11, 20), (11, 19)],
    [(12, 20), (12, 19), (13, 20), (13, 19), (14, 20), (14, 19), (15, 20), (15, 19)],
    [(16, 20), (16, 19), (17, 20), (17, 19), (23, 20), (23, 19), (24, 20), (24, 19)],
    [(25, 20), (25, 19), (26, 20), (26, 19), (26, 18), (26, 17), (25, 18), (25, 17)],
    [(24, 18), (24, 17), (23, 18), (23, 17), (22, 17), (21, 17), (20, 17), (19, 17)],
    [(18, 17), (17, 18), (17, 17), (16, 18), (16, 17), (15, 18), (15, 17), (14, 18)],
    [(14, 17), (13, 18), (13, 17), (12, 18), (12, 17), (11, 18), (11, 17), (10, 18)],
    [(10, 17), (9, 18), (9, 17), (7, 18), (7, 17), (6, 18), (6, 17), (5, 18)],
    [(5, 17), (4, 18), (4, 17), (3, 18), (3, 17), (2, 18), (2, 17), (2, 16)],
    [(2, 15), (3, 16), (3, 15), (4, 16), (4, 15), (5, 16), (5, 15), (6, 16)],
    [(6, 15), (7, 16), (7, 15), (9, 16), (9, 15), (10, 16), (10, 15), (11, 16)],
    [(11, 15), (12, 16), (12, 15), (13, 16), (13, 15), (14, 16), (14, 15), (15, 16)],
    [(15, 15), (16, 16), (16, 15), (17, 16), (17, 15), (18, 16), (18, 15), (19, 16)],
    [(19, 15), (20, 16), (20, 15), (21, 16), (21, 15), (22, 16), (22, 15), (23, 16)],
    [(23, 15), (24, 16), (24, 15), (25, 16), (25, 15), (26, 16), (26, 15), (26, 14)],
    [(26, 13), (25, 14), (25, 13), (24, 14), (24, 13), (23, 14), (23, 13), (22, 14)],
    [(22, 13), (21, 14), (21, 13), (20, 14), (20, 13), (19, 14), (19, 13), (18, 14)],
    [(18, 13), (17, 14), (17, 13), (16, 14), (16, 13), (15, 14), (15, 13), (14, 14)],
    [(14, 13), (13, 14), (13, 13), (12, 14), (12, 13), (11, 14), (11, 13), (10, 14)],
    [(10, 13), (9, 14), (9, 13), (7, 14), (7, 13), (6, 14), (6, 13), (5, 14)],
    [(5, 13), (4, 14), (4, 13), (3, 14), (3, 13), (2, 14), (2, 13), (2, 12)],
    [(2, 11), (3, 12), (3, 11), (4, 12), (4, 11), (5, 12), (5, 11), (6, 12)],
    [(6, 11), (7, 12), (7, 11), (9, 12), (9, 11), (10, 12), (10, 11), (11, 12)],
    [(11, 11), (12, 12), (12, 11), (13, 12), (13, 11), (14, 12), (14, 11), (15, 12)],
    [(15, 11), (16, 12), (16, 11), (17, 12), (17, 11), (18, 12), (18, 11), (19, 12)],
    [(19, 11), (20, 12), (20, 11), (21, 12), (21, 11), (22, 12), (22, 11), (23, 12)],
    [(23, 11), (24, 12), (24, 11), (25, 12), (25, 11), (26, 12), (26, 11), (18, 10)],
    [(18, 9), (17, 10), (17, 9), (16, 10), (16, 9), (15, 10), (15, 9), (14, 10)],
    [(14, 9), (13, 10), (13, 9), (12, 10), (12, 9), (11, 10), (11, 9), (11, 7)],
    [(11, 6), (12, 7), (12, 6), (13, 7), (13, 6), (14, 7), (14, 6), (15, 7)],
    [(15, 6), (16, 7), (16, 6), (17, 7), (17, 6), (18, 7), (18, 6), (18, 5)],
    [(18, 4), (17, 5), (17, 4), (16, 5), (16, 4), (15, 5), (15, 4), (14, 5)],
    [(14, 4), (13, 5), (13, 4), (12, 5), (12, 4), (11, 5), (11, 4), (11, 3)],
    [(11, 2), (12, 3), (12, 2), (13, 3), (13, 2), (14, 3), (14, 2), (15, 3)],
]

moderror_counts = []
symerror_counts = []

def main():
    directory = "C:\\Users\\ikuma\\QR-decoding\\data\\resourse"
    clear_csv_file()

    for i in range(200):
        print(i)
        original_path = os.path.join(directory, 'original', f'{i}.png')
        denoised_path = os.path.join(directory, 'denoised', '9.9', f'{i}.png')
        process_image(original_path, denoised_path)

def process_image(original_path, denoised_path):
    # 画像を読み込む
    original_image = cv2.imread(original_path, cv2.IMREAD_GRAYSCALE)
    denoised_image = cv2.imread(denoised_path, cv2.IMREAD_GRAYSCALE)

    # 29×29にリサイズ
    resized_original_image = cv2.resize(original_image, (29, 29))
    resized_denoised_image = cv2.resize(denoised_image, (29, 29))

    moderror_count, symerror_count, error_symbols = calculate_errors(resized_original_image, resized_denoised_image)

    # エラーカウントをリストに追加
    moderror_counts.append(moderror_count)
    symerror_counts.append(symerror_count)

    # エラーカウントを表示
    print(f"error_module for {moderror_count}")
    print(f"error_symbol for {symerror_count}")
    print(f"Error Symbols: {error_symbols}")
    print()

    # Error SymbolsをCSVファイルに保存
    save_error_symbols_to_csv(error_symbols)

def calculate_errors(original_image, denoised_image):
    moderror_count = 0
    symerror_count = 0
    counted_symbols = set()
    error_symbols = []

    for i in range(29):
        for j in range(29):
            denoised_value = denoised_image[i, j]
            original_value = original_image[i, j]

            if (original_value == 255 and denoised_value <= 139) or (original_value == 0 and denoised_value >= 81):
                moderror_count += 1

                # シンボルの各座標を確認
                for symbol_index, symbol in enumerate(symbols):
                    if (i, j) in symbol and symbol_index not in counted_symbols:
                        symerror_count += 1
                        counted_symbols.add(symbol_index)
                        error_symbols.append(symbol_index)

    # 次の繰り返しのためにセットをクリア
    counted_symbols.clear()

    return moderror_count, symerror_count, error_symbols

def save_error_symbols_to_csv(error_symbols):
    csv_file_path = 'error_symbols.csv'
    with open(csv_file_path, mode='a', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(error_symbols)

def clear_csv_file():
    csv_file_path = 'error_symbols.csv'
    open(csv_file_path, 'w').close()

if __name__ == "__main__":
    main()