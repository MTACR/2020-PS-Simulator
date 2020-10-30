public class TestOnly {

    public static enum OPCODE {
        ADD(2),
        BR(0),
        BRNEG(5),
        BRPOS(1),
        BRZERO(4),
        CALL(15),
        COPY(13),
        DIVIDE(10),
        LOAD(2),
        MULT(14),
        READ(12),
        RET(9),
        STOP(11),
        STORE(0),
        SUB(6),
        WRITE(8);

        private final int op;

        OPCODE(int op) {
            this.op = op;
        }
    }

    private short[] memory = new short[1024];

    private void dummy(OPCODE opcode) {
        switch (opcode) {
            case ADD:
                break;
            case BR:
                break;
            case BRNEG:
                break;
            case BRPOS:
                break;
            case BRZERO:
                break;
            case CALL:
                break;
            case COPY:
                break;
            case DIVIDE:
                break;
            case LOAD:
                break;
            case MULT:
                break;
            case READ:
                break;
            case RET:
                break;
            case STOP:
                break;
            case STORE:
                break;
            case SUB:
                break;
            case WRITE:
                break;
        }
    }

}
