package org.blockartistry.mod.Restructured.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class MyNBTTagByte extends MyNBTPrimitive {
	
	private byte data;

	MyNBTTagByte() {
	}

	public MyNBTTagByte(byte value) {
		this.data = value;
	}

	@Override
	void writeStream(DataOutput stream) throws IOException {
		stream.writeByte(this.data);
	}

	@Override
	void readStream(DataInput stream, int depth, MyNBTSizeTracker tracker) throws IOException {
		tracker.func_152450_a(8L);
		this.data = stream.readByte();
	}

	public byte getId() {
		return 1;
	}

	public String toString() {
		return "" + this.data + "b";
	}

	public MyNBTBase copy() {
		return MyNBTBase.class.cast(new MyNBTTagByte(this.data));
	}

	public boolean equals(Object p_equals_1_) {
		if (super.equals(p_equals_1_)) {
			MyNBTTagByte localNBTTagByte = (MyNBTTagByte) p_equals_1_;
			return this.data == localNBTTagByte.data;
		}
		return false;
	}

	public int hashCode() {
		return super.hashCode() ^ this.data;
	}

	public long func_150291_c() {
		return this.data;
	}

	public int func_150287_d() {
		return this.data;
	}

	public short func_150289_e() {
		return (short) this.data;
	}

	public byte func_150290_f() {
		return this.data;
	}

	public double func_150286_g() {
		return this.data;
	}

	public float func_150288_h() {
		return this.data;
	}
}
