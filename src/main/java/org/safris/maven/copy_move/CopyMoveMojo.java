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
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.openjax.maven.mojo.AnnotationUtil;
import org.sonatype.plexus.build.incremental.BuildContext;

public abstract class CopyMoveMojo extends AbstractMojo {
  /**
   * The source file or directory.
   *
   * @since 1.0
   */
  @Parameter
  private File sourceFile;

  /**
   * The target file or directory.
   *
   * @since 1.0
   */
  @Parameter
  private File destinationFile;

  /**
   * Collection of {@code FileSet}s to work on ({@code FileSet} contains
   * {@code sourceFile} and {@code destinationFile}). See
   * <a href="./usage.html">Usage</a> for details.
   *
   * @since 1.0
   */
  @Parameter
  private List<FileSet> fileSets;

  /**
   * @since 1.0
   */
  @Parameter(defaultValue="${project}", readonly=true, required=true)
  private MavenProject project;

  /**
   * @since 1.0
   */
  @Component
  private BuildContext buildContext;

  /**
   * Whether to overwrite files.
   *
   * @since 1.0
   */
  @Parameter(property="copy-move.overWrite", defaultValue="true")
  private boolean overWrite;

  /**
   * Ignore File Not Found errors during incremental build
   *
   * @since 1.0
   */
  @Parameter(property="copy-move.ignoreFileNotFoundOnIncremental", defaultValue="true")
  private boolean ignoreFileNotFoundOnIncremental;

  @Override
  public final void execute() throws MojoExecutionException {
    getLog().debug("Executing the copy-move-maven-plugin");
    if (fileSets != null && fileSets.size() > 0) {
      for (final FileSet fileSet : fileSets) {
        execute(fileSet.getSourceFile(), fileSet.getDestinationFile());
      }
    }
    else if (sourceFile != null || destinationFile != null) {
      execute(sourceFile, destinationFile);
    }
    else {
      getLog().info("No Files to process");
    }
  }

  private void execute(final File srcFile, final File destFile) throws MojoExecutionException {
    if (srcFile == null)
      throw new MojoExecutionException("sourceFile not specified");

    if (destFile == null)
      throw new MojoExecutionException("destinationFile not specified");

    try {
      if (srcFile.exists()) {
        if (destFile.exists() && !overWrite) {
          getLog().warn(destFile.getAbsolutePath() + " already exists and overWrite not set");
        }
        else if (allowIncremental() && buildContext.isIncremental() && destFile.exists() && !buildContext.hasDelta(srcFile) && FileUtils.contentEquals(srcFile, destFile)) {
          getLog().info("No changes detected in " + srcFile.getAbsolutePath());
        }
        else {
          destFile.getParentFile().mkdirs();
          execute(srcFile, destFile, buildContext);
        }
      }
      else if (ignoreFileNotFoundOnIncremental && buildContext.isIncremental()) {
        getLog().warn("sourceFile " + srcFile.getAbsolutePath() + " not found during incremental build");
      }
      else {
        getLog().warn("sourceFile " + srcFile.getAbsolutePath() + " does not exist");
      }
    }
    catch (final IOException e) {
      String action;
      try {
        action = AnnotationUtil.getAnnotationParameters(getClass(), Mojo.class).name();
      }
      catch (final IOException ignore) {
        action = "copy/move";
      }

      throw new MojoExecutionException("could not " + action + " " + srcFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
    }
  }

  abstract boolean allowIncremental();
  abstract void execute(File srcFile, File destFile, BuildContext buildContext) throws IOException;
}