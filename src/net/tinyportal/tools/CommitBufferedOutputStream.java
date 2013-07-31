/*
    This file is part of tPortal.

    tPortal is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    tPortal is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with tPortal.  If not, see <http://www.gnu.org/licenses/>.

    The original code was written by Sebastien Bettinger <sebastien.bettinger@gmail.com>

 */

package net.tinyportal.tools;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class CommitBufferedOutputStream extends BufferedOutputStream implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4828002484747518385L;
	/**
	 * Flag indiquant si le buffer a été commité ou non
	 */
	protected boolean commit = false;

	/**
	 * Creates a new buffered output stream to write data to the
	 * specified underlying output stream.
	 *
	 * @param   out   the underlying output stream.
	 */
	public CommitBufferedOutputStream(OutputStream out) {
		this(out, 8192);
	}

	/**
	 * Creates a new buffered output stream to write data to the 
	 * specified underlying output stream with the specified buffer 
	 * size. 
	 *
	 * @param   out    the underlying output stream.
	 * @param   size   the buffer size.
	 * @exception IllegalArgumentException if size &lt;= 0.
	 */
	public CommitBufferedOutputStream(OutputStream out, int size) {
		super(out);
		if (size <= 0) {
			throw new IllegalArgumentException("Buffer size <= 0");
		}
		buf = new byte[size];
	}

	/** Flush the internal buffer */
	private void flushBuffer() throws IOException {
		commit = true;
		if (count > 0) {
			out.write(buf, 0, count);
			count = 0;
		}
	}

	/**
	 * Writes the specified byte to this buffered output stream. 
	 *
	 * @param      b   the byte to be written.
	 * @exception  IOException  if an I/O error occurs.
	 */
	public synchronized void write(int b) throws IOException {
		if (count >= buf.length) {
			flushBuffer();
		}
		buf[count++] = (byte)b;
	}

	/**
	 * Writes <code>len</code> bytes from the specified byte array 
	 * starting at offset <code>off</code> to this buffered output stream.
	 *
	 * <p> Ordinarily this method stores bytes from the given array into this
	 * stream's buffer, flushing the buffer to the underlying output stream as
	 * needed.  If the requested length is at least as large as this stream's
	 * buffer, however, then this method will flush the buffer and write the
	 * bytes directly to the underlying output stream.  Thus redundant
	 * <code>BufferedOutputStream</code>s will not copy data unnecessarily.
	 *
	 * @param      b     the data.
	 * @param      off   the start offset in the data.
	 * @param      len   the number of bytes to write.
	 * @exception  IOException  if an I/O error occurs.
	 */
	public synchronized void write(byte b[], int off, int len) throws IOException {
		if (len >= buf.length) {
			/* If the request length exceeds the size of the output buffer,
	    	       flush the output buffer and then write the data directly.
	    	       In this way buffered streams will cascade harmlessly. */
			flushBuffer();
			out.write(b, off, len);
			return;
		}
		if (len > buf.length - count) {
			flushBuffer();
		}
		System.arraycopy(b, off, buf, count, len);
		count += len;
	}

	/**
	 * Flushes this buffered output stream. This forces any buffered 
	 * output bytes to be written out to the underlying output stream. 
	 *
	 * @exception  IOException  if an I/O error occurs.
	 * @see        java.io.FilterOutputStream#out
	 */
	public synchronized void flush() throws IOException {
		flushBuffer();
		out.flush();
	}


	public boolean isCommitted() {
		return commit;
	}

	public String toString() {
		return this.out.toString();
	}

	public int getBufferSize() {
		return buf.length;
	}

	/**
	 * Positionne la taille du buffer
	 * @param size taille en byte
	 * @throws IllegalStateException Si une écriture a déjà été réalisée
	 * une IllegalStateException est levé
	 */
	public void setBufferSize(int size) {
		if (commit || count > 0) throw new IllegalStateException();
		//		flush();
		buf = new byte[size];
		count = 0;
		commit = false;
	}
}
