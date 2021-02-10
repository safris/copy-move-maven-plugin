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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * Move files or directories during build.
 */
@Mojo(name="move", defaultPhase=LifecyclePhase.GENERATE_SOURCES, threadSafe=true)
public class MoveMojo extends CopyMoveMojo {
  @Override
  boolean allowIncremental() {
    return false;
  }

  @Override
  void execute(final File srcFile, final File destFile, final BuildContext buildContext) throws IOException {
    Files.move(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    getLog().info("Moved " + srcFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
    buildContext.refresh(destFile);
  }
}