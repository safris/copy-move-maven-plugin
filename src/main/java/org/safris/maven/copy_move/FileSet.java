/* Copyright (c) 2021 Seva Safris
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.safris.maven.copy_move;

import java.io.File;

public class FileSet {
  private File sourceFile;
  private File destinationFile;

  public File getSourceFile() {
    return sourceFile;
  }

  public void setSourceFile(final File sourceFile) {
    this.sourceFile = sourceFile;
  }

  public File getDestinationFile() {
    return destinationFile;
  }

  public void setDestinationFile(final File destinationFile) {
    this.destinationFile = destinationFile;
  }
}