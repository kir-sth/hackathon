package scraper.models;

public enum PostType {
    Text(0),
    Photo(1),
    Video(2),
    Audio(3),
    Document(4),
    Multimedia(5),

    Other(-1),
    ChannelInfo(-2);

    private final int typeId;

    PostType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public static PostType parse(Integer typeId) {
        if (typeId == null) {
            return Other;
        }
        return switch (typeId) {
            case 0 -> Text;
            case 1 -> Photo;
            case 2 -> Video;
            case 3 -> Audio;
            case 4 -> Document;
            case 5 -> Multimedia;
            default -> Other;
        };
    }}
