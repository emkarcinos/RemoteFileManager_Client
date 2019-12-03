public class Protocol {
    /* CLIENT MESSAGES */

    /* Allows for data transfer */
    public static String C_ACK = "33986ack";
    /* Asks for directory list */
    public static String C_DIR = "39235sendmedir";
    /* Asks for a file number */
    public static String C_FILE = "62384filenumber";
    /* Yes */
    public static String C_YES = "12385yesans";
    /* No */
    public static String C_NO = "98632noans";
    /* Tells goodbye */
    public static String C_STOP = "34569goodbye";

    /* SERVER MESSAGES */

    /* Begin Directory list stream */
    public static String DIR_START = "48375dirstart";
    /* End Directory list stream */
    public static String DIR_DONE = "35236dirdone";
    /* Begin File stream */
    public static String FILE_START = "83568filestart";
    /* End File stream */
    public static String FILE_DONE = "59837filedone";
    /* File Error */
    public static String FILE_ERR = "14987fileerr";
    /* Dir Error */
    public static String DIR_ERR = "39562direrr";
}
