package view;

/**
 * ASCII {@code enum} with a {@code String} to {@code int} hexadecimal representation map.
 * Used to display ASCII characters in console.
 */
public enum ASCII {
    /** U+2563 ╣ - Box vertical and left */
    BVL(0x2563),
    /** U+2551 ║ - Box vertical */
    BV(0x2551),
    /** U+2557 ╗ - Box down and left */
    BDL(0x2557),
    /** U+255D   - Box up and left */
    BUL(0x255D),
    /** U+255A ╚ - Box up and right */
    BUR(0x255A),
    /** U+2554 ╔ - Box down and right */
    BDR(0x2554),
    /** U+2569 ╩ - Box up and horizontal */
    BUH(0x2569),
    /** U+2566 ╦ - Box down and horizontal */
    BDH(0x2566),
    /** U+2560 ╠ - Box vertical and right */
    BVR(0x2560),
    /** U+2550   - Box horizontal */
    BH(0x2550),
    /** U+256C ╬ - Box vertical and horizontal */
    BVH(0x256C),

    /** U+03A6 Φ - Greek capital letter phi */
    GP(0x03A6),
    /** U+221E ∞ - Infinity */
    INF(0x221E),
    /** U+2261 ≡ - Identical to */
    IT(0x2261),
    /** U+25A0 ■ - Black square */
    BS(0x25A0),
    /** U+2718 ■ - Heavy ballot X (bold cross) */
    CROSS(0x2718);

    /**
     * {@code int} containing the hexadecimal representation of the ASCII
     */
    private final int asciiCode;

    /**
     * Store the hexadecimal representation of the ASCII
     *
     * @param asciiCode The ASCII hexadecimal code
     */
    ASCII(int asciiCode) { this.asciiCode = asciiCode; }

    /**
     * Pass the ascii hexadecimal {@code int} code into a {@code char} and then to a {@code String} in order for it to be read by the console
     *
     * @return The ascii character as a {@code String}
     */
    @Override
    public String toString() { return new String(Character.toChars(asciiCode)); }
}