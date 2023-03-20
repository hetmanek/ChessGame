package enums;

public enum PieceType {
    K(0),
    Q(1),
    B(2),
    N(3),
    R(4),
    P(5),
    k(6),
    q(7),
    b(8),
    n(9),
    r(10),
    p(11);
    private final int imageIndex;

    PieceType(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public int getImageIndex() {
        return this.imageIndex;
    }
}
