/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.ballerinalang.packerina.cmd;

import org.ballerinalang.compiler.CompilerPhase;
import org.ballerinalang.packerina.TaskExecutor;
import org.ballerinalang.packerina.buildcontext.BuildContext;
import org.ballerinalang.packerina.buildcontext.BuildContextField;
import org.ballerinalang.packerina.task.CleanTargetDirTask;
import org.ballerinalang.packerina.task.CompileTask;
import org.ballerinalang.packerina.task.CopyExecutableTask;
import org.ballerinalang.packerina.task.CopyModuleJarTask;
import org.ballerinalang.packerina.task.CopyNativeLibTask;
import org.ballerinalang.packerina.task.CreateBaloTask;
import org.ballerinalang.packerina.task.CreateBirTask;
import org.ballerinalang.packerina.task.CreateDocsTask;
import org.ballerinalang.packerina.task.CreateExecutableTask;
import org.ballerinalang.packerina.task.CreateJarTask;
import org.ballerinalang.packerina.task.CreateLockFileTask;
import org.ballerinalang.packerina.task.CreateTargetDirTask;
import org.ballerinalang.packerina.task.PrintExecutablePathTask;
import org.ballerinalang.packerina.task.RunCompilerPluginTask;
import org.ballerinalang.packerina.task.RunTestsTask;
import org.ballerinalang.tool.BLauncherCmd;
import org.ballerinalang.tool.LauncherUtils;
import org.ballerinalang.util.BLangConstants;
import org.wso2.ballerinalang.compiler.util.CompilerContext;
import org.wso2.ballerinalang.compiler.util.CompilerOptions;
import org.wso2.ballerinalang.compiler.util.ProjectDirConstants;
import org.wso2.ballerinalang.compiler.util.ProjectDirs;
import org.wso2.ballerinalang.util.RepoUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.ballerinalang.compiler.CompilerOptionName.COMPILER_PHASE;
import static org.ballerinalang.compiler.CompilerOptionName.EXPERIMENTAL_FEATURES_ENABLED;
import static org.ballerinalang.compiler.CompilerOptionName.LOCK_ENABLED;
import static org.ballerinalang.compiler.CompilerOptionName.OFFLINE;
import static org.ballerinalang.compiler.CompilerOptionName.PROJECT_DIR;
import static org.ballerinalang.compiler.CompilerOptionName.SIDDHI_RUNTIME_ENABLED;
import static org.ballerinalang.compiler.CompilerOptionName.SKIP_TESTS;
import static org.ballerinalang.compiler.CompilerOptionName.TEST_ENABLED;
import static org.ballerinalang.packerina.buildcontext.sourcecontext.SourceType.SINGLE_BAL_FILE;
import static org.ballerinalang.packerina.cmd.Constants.BUILD_COMMAND;

/**
 * This class represents the "ballerina build" command.
 *
 * @since 0.90
 */
@CommandLine.Command(name = BUILD_COMMAND, description = "Ballerina build - Builds Ballerina module(s) and generates " +
                                                         "executable outputs.")
public class BuildCommand implements BLauncherCmd {
    
    private final PrintStream outStream;
    private final PrintStream errStream;
    private Path sourceRootPath;
    private boolean exitWhenFinish;

    public BuildCommand() {
        sourceRootPath = Paths.get(System.getProperty("user.dir"));
        outStream = System.out;
        errStream = System.err;
        exitWhenFinish = true;
    }

    public BuildCommand(Path userDir, PrintStream outStream, PrintStream errStream, boolean exitWhenFinish) {
        this.sourceRootPath = userDir;
        this.outStream = outStream;
        this.errStream = errStream;
        this.exitWhenFinish = exitWhenFinish;
    }

    @CommandLine.Option(names = {"--output", "-o"}, description = "write executable output to the given file")
    private String output;

    @CommandLine.Option(names = {"--off-line"})
    private boolean offline;

    @CommandLine.Option(names = {"--skip-lock"})
    private boolean skipLock;

    @CommandLine.Option(names = {"--skip-tests"})
    private boolean skipTests;

    @CommandLine.Parameters
    private List<String> argList;

    @CommandLine.Option(names = {"--native"}, hidden = true,
            description = "compile Ballerina program to a native binary")
    private boolean nativeBinary;

    @CommandLine.Option(names = "--dump-bir", hidden = true)
    private boolean dumpBIR;

    @CommandLine.Option(names = "--dump-llvm-ir", hidden = true)
    private boolean dumpLLVMIR;

    @CommandLine.Option(names = {"--help", "-h"}, hidden = true)
    private boolean helpFlag;

    @CommandLine.Option(names = "--experimental", description = "enable experimental language features")
    private boolean experimentalFlag;

    @CommandLine.Option(names = {"--config"}, description = "path to the configuration file")
    private String configFilePath;

    @CommandLine.Option(names = "--siddhi-runtime", description = "enable siddhi runtime for stream processing")
    private boolean siddhiRuntimeFlag;

    public void execute() {
        if (this.helpFlag) {
            String commandUsageInfo = BLauncherCmd.getCommandUsageInfo(BUILD_COMMAND);
            this.errStream.println(commandUsageInfo);
            return;
        }
        
        if (this.argList != null && this.argList.size() > 1) {
            CommandUtil.printError(this.errStream,
                    "too many arguments.",
                    "ballerina build [<module-name>]",
                    true);
        }
        
        if (this.nativeBinary) {
            throw LauncherUtils.createLauncherException("llvm native generation is not supported");
        }
    
        // validation and decide source root and source full path
        Path sourcePath = null;
        Path targetPath;
        
        // when no bal file or module is given, it is assumed to build all modules of the project. check if the command
        // is executed within a ballerina project. update source root path if command executed inside a project.
        if (this.argList == null || this.argList.size() == 0) {
            // when building all the modules
            //// check if output flag is set
            if (null != this.output) {
                throw LauncherUtils.createLauncherException("'-o' and '--output' flag is only supported for building" +
                                                            " a single ballerina file.");
            }
        
            // validate and set source root path
            if (!ProjectDirs.isProject(sourceRootPath)) {
                Path findRoot = ProjectDirs.findProjectRoot(sourceRootPath);
                if (null == findRoot) {
                    CommandUtil.printError(this.errStream,
                            "please provide a Ballerina file as a " +
                            "input or run build command inside a project",
                            "ballerina build [<filename.bal>]",
                            false);
                    return;
                }
                sourceRootPath = findRoot;
            }
        
            targetPath = sourceRootPath.resolve(ProjectDirConstants.TARGET_DIR_NAME);
        } else if (this.argList.get(0).endsWith(BLangConstants.BLANG_SRC_FILE_SUFFIX)) {
            // when a single bal file is provided.
            
            //// check if path given is an absolute path. update source root accordingly.
            if (Paths.get(this.argList.get(0)).isAbsolute()) {
                sourcePath = Paths.get(this.argList.get(0));
                sourceRootPath = sourcePath.getParent();
            } else {
                sourcePath = sourceRootPath.resolve(this.argList.get(0));
            }
            
            //// check if the given file exists.
            if (Files.notExists(sourcePath)) {
                throw LauncherUtils.createLauncherException("'" + sourcePath + "' ballerina file does not exist.");
            }
            
            //// check if the given file is a regular file and not a symlink.
            if (!Files.isRegularFile(sourcePath)) {
                throw LauncherUtils.createLauncherException("'" + sourcePath + "' is not ballerina file.");
            }
    
            try {
                targetPath = Files.createTempDirectory("ballerina-build-" + System.nanoTime());
            } catch (IOException e) {
                throw LauncherUtils.createLauncherException("error occurred when creating executable.");
            }
        } else {
            // when building a ballerina module
            //// output flag cannot be set for projects
            if (null != this.output) {
                throw LauncherUtils.createLauncherException("'-o' and '--output' flag is only supported for building" +
                                                            " a single ballerina file.");
            }
            
            //// check if command executed from project root.
            if (!RepoUtils.isBallerinaProject(sourceRootPath)) {
                throw LauncherUtils.createLauncherException("you are trying to build a module that is not inside " +
                                                            "a project. Run `ballerina new` from " +
                                                            sourceRootPath + " to initialize it as a " +
                                                            "project and then build the module.");
            }
            
            //// check if module name given is not absolute.
            if (Paths.get(argList.get(0)).isAbsolute()) {
                throw LauncherUtils.createLauncherException("you are trying to build a module by giving the absolute" +
                                                            " path. you only need give the name of the module.");
            }
    
            String moduleName = argList.get(0);
    
            //// remove end forward slash
            if (moduleName.endsWith("/")) {
                moduleName = moduleName.substring(0, moduleName.length() - 1);
            }
            
            sourcePath = Paths.get(moduleName);
            
            //// check if module exists.
            if (Files.notExists(sourceRootPath.resolve(ProjectDirConstants.SOURCE_DIR_NAME).resolve(sourcePath))) {
                throw LauncherUtils.createLauncherException("'" + sourcePath + "' module does not exist.");
            }
    
            targetPath = sourcePath.resolve(ProjectDirConstants.TARGET_DIR_NAME);
        }
        
        // normalize paths
        sourceRootPath = sourceRootPath.normalize();
        sourcePath = sourcePath == null ? null : sourcePath.normalize();
        targetPath = targetPath.normalize();
    
        // create compiler context
        CompilerContext context = new CompilerContext();
        CompilerOptions options = CompilerOptions.getInstance(context);
        options.put(PROJECT_DIR, sourceRootPath.toString());
        options.put(OFFLINE, Boolean.toString(this.offline));
        options.put(COMPILER_PHASE, CompilerPhase.BIR_GEN.toString());
        options.put(LOCK_ENABLED, Boolean.toString(!this.skipLock));
        options.put(SKIP_TESTS, Boolean.toString(this.skipTests));
        options.put(TEST_ENABLED, "true");
        options.put(EXPERIMENTAL_FEATURES_ENABLED, Boolean.toString(this.experimentalFlag));
        options.put(SIDDHI_RUNTIME_ENABLED, Boolean.toString(this.siddhiRuntimeFlag));
    
        // create builder context
        BuildContext buildContext = new BuildContext(sourceRootPath, targetPath, sourcePath);
        buildContext.setOut(outStream);
        buildContext.setErr(errStream);
        buildContext.put(BuildContextField.COMPILER_CONTEXT, context);
    
        boolean isSingleFileBuild = buildContext.getSourceType().equals(SINGLE_BAL_FILE);
        Path outputPath = null == this.output ? null : Paths.get(this.output);
        
        TaskExecutor taskExecutor = new TaskExecutor.TaskBuilder()
                .addTask(new CleanTargetDirTask(), isSingleFileBuild)   // clean the target directory(projects only)
                .addTask(new CreateTargetDirTask()) //  create target directory.
                .addTask(new CompileTask()) // compile the modules
                .addTask(new CreateBaloTask(), isSingleFileBuild)   // create the balos for modules(projects only)
                .addTask(new CreateBirTask())   // create the bir
                .addTask(new CopyNativeLibTask(), isSingleFileBuild)    // copy the native libs(projects only)
                .addTask(new CreateJarTask(this.dumpBIR))    // create the jar
                .addTask(new CopyModuleJarTask())
                .addTask(new RunTestsTask(), this.skipTests || isSingleFileBuild)   // run tests(projects only)
                .addTask(new CreateExecutableTask())    // create the executable .jar file
                .addTask(new CopyExecutableTask(outputPath), !isSingleFileBuild)    // copy executable
                .addTask(new PrintExecutablePathTask()) // print the location of the executable
                .addTask(new CreateLockFileTask(), isSingleFileBuild)   // create a lock file(projects only)
                .addTask(new CreateDocsTask(), isSingleFileBuild)   // generate API docs(projects only)
                .addTask(new RunCompilerPluginTask())   // run compiler plugins
                .addTask(new CleanTargetDirTask(), !isSingleFileBuild)  // clean the target dir(single bals only)
                .build();
        
        taskExecutor.executeTasks(buildContext);
        
        if (exitWhenFinish) {
            Runtime.getRuntime().exit(0);
        }
    }

    @Override
    public String getName() {
        return BUILD_COMMAND;
    }

    @Override
    public void printLongDesc(StringBuilder out) {
        out.append("Builds Ballerina module(s)/file and produces an executable jar file(s). \n");
        out.append("\n");
        out.append("Building a Ballerina project or a specific module in a project the \n");
        out.append("executable \".jar\" files will be created in <project-root>/target/bin directory. \n");
        out.append("\n");
        out.append("Building a single Ballerina file will create an executable .jar file in the \n");
        out.append("current directory. The name of the executable file will be. \n");
        out.append("<bal-file-name>-executable.jar. \n");
        out.append("\n");
        out.append("If the output file is specified with the -o flag, the output \n");
        out.append("will be written to the given output file name. The -o flag will only \n");
        out.append("work for single files. \n");
    }

    @Override
    public void printUsage(StringBuilder out) {
        out.append("  ballerina build [-o <output>] [--off-line] [--skip-tests] [--skip-lock] " +
                   "[<bal-file | module-name>] \n");
    }

    @Override
    public void setParentCmdParser(CommandLine parentCmdParser) {
    }
}
