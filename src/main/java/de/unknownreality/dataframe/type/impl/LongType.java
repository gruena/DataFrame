package de.unknownreality.dataframe.type.impl;

import de.unknownreality.dataframe.column.settings.ColumnSettings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;

public class LongType extends NumberType<Long> {
    public LongType(ColumnSettings columnSettings) {
        super(columnSettings);
    }

    @Override
    public Class<Long> getType() {
        return Long.class;
    }


    @Override
    public Long read(DataInputStream dis) throws IOException {
        return dis.readLong();
    }

    @Override
    public Long read(ByteBuffer buf) {
        return buf.getLong();
    }

    @Override
    public Long parse(String s) {
        return Long.parseLong(s);
    }

    @Override
    public void write(Writer writer, Long value) throws IOException {
        writer.write(toString(value));
    }

    @Override
    public String toString(Long value) {
        return Long.toString(value);
    }


    @Override
    public int write(DataOutputStream dos, Long value) throws IOException {
        dos.writeLong(value);
        return Long.BYTES;
    }
}