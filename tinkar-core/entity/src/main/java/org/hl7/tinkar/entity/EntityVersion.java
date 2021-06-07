package org.hl7.tinkar.entity;

import io.activej.bytebuf.ByteBuf;
import io.activej.bytebuf.ByteBufPool;
import org.hl7.tinkar.common.id.PublicId;
import org.hl7.tinkar.component.Stamp;
import org.hl7.tinkar.component.Version;
import org.hl7.tinkar.component.FieldDataType;

public abstract class EntityVersion
        implements Version {
    protected Entity chronology;
    protected int stampNid;

    protected EntityVersion() { }

    public abstract FieldDataType dataType();

    public final int nid() {
        return chronology.nid();
    }

    protected final void fill(Entity chronology, ByteBuf readBuf, byte formatVersion) {
        this.chronology = chronology;
        int versionArraySize = readBuf.readInt();
        byte token = readBuf.readByte();
        if (dataType().token != token) {
            throw new IllegalStateException("Wrong token for type: " + token);
        }
        this.stampNid = readBuf.readInt();
        finishVersionFill(readBuf, formatVersion);
    }

    protected final void fill(Entity chronology, Version version) {
        this.chronology = chronology;
        Stamp stamp = version.stamp();
        this.stampNid = EntityService.get().nidForComponent(stamp);
    }

    protected abstract void finishVersionFill(ByteBuf readBuf, byte formatVersion);
    
    protected int versionSize() {
        return 9 + subclassFieldBytesSize(); // token, stamp, field count
    }

    protected abstract int subclassFieldBytesSize();

    protected final byte[] getBytes() {
        ByteBuf byteBuf = ByteBufPool.allocate(versionSize());
        byteBuf.writeByte(dataType().token); //ensure that the chronicle byte array sorts first.
        byteBuf.writeInt(stampNid);
        writeVersionFields(byteBuf);
        return byteBuf.asArray();
    }

    protected abstract void writeVersionFields(ByteBuf writeBuf);

    public Entity chronology() {
        return chronology;
    }

    @Override
    public PublicId publicId() {
        return chronology.publicId();
    }

    @Override
    public StampEntity stamp() {
        return EntityService.get().getStampFast(stampNid);
    }

    public int stampNid() {
        return stampNid;
    }

    @Override
    public String toString() {
        return stamp().describe();
    }
}
