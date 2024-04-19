package com.scraper.models;

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


}
