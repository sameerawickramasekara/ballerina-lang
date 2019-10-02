
public function hugeArrayTest() returns int[] {
    int[] array = [8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4,
                   8, 3, 6, 9, 1, 0, 7, 2, 4, 5, 8, 3, 6, 9, 1, 0, 7, 2, 4, 5, 8, 3, 6, 9, 1, 0, 7, 2, 4, 5,
                   3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5,
                   8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4,
                   3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5,
                   8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4,
                   3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 7, 2, 4, 5, 8, 3, 6, 9, 1, 0, 7, 2, 4, 5, 8, 3, 6, 9, 1, 0, 7, 2, 4, 5,
                   3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5,
                   8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4,
                   8, 3, 6, 9, 1, 0, 7, 2, 4, 5, 8, 3, 6, 9, 1, 0, 7, 2, 4, 5, 8, 3, 6, 9, 1, 0, 7, 2, 4, 5,
                   3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5,
                   8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4,
                   8, 3, 6, 9, 1, 0, 7, 2, 4, 5, 8, 3, 6, 9, 1, 0, 7, 2, 4, 5, 8, 3, 6, 9, 1, 0, 7, 2, 4, 5,
                   3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5,
                   8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4, 8, 3, 6, 9, 1, 0, 7, 2, 6, 4,
                   8, 3, 6, 9, 1, 0, 7, 2, 4, 5, 8, 3, 6, 9, 1, 0, 7, 2, 4, 5, 8, 3, 6, 9, 1, 0, 7, 2, 4, 5,
                   3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5, 3, 6, 9, 1, 0, 7, 2, 6, 4, 5,
                   8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5, 8, 3, 6, 9, 1, 0, 2, 6, 4, 5,
                   8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5, 8, 3, 9, 1, 0, 7, 2, 6, 4, 5,
                   1,1,1,1,1,1];
    return array;
}