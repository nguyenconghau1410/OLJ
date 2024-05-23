package com.example.oj.controller;

import com.example.oj.constant.Utils;
import com.example.oj.document.ProblemDocument;
import com.example.oj.document.SubmissionDocument;
import com.example.oj.dto.Execute;
import com.example.oj.dto.ResultTestCase;
import com.example.oj.dto.Submission;
import com.example.oj.service.ProblemService;
import com.example.oj.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@Controller
@RequiredArgsConstructor
public class SubmissionController {

    private final SimpMessagingTemplate messagingTemplate;
    private final SubmissionService submissionService;
    private final ProblemService problemService;
    private final Utils utils;
    @MessageMapping("/execute")
    public void executeCode(@Payload String id) throws IOException, ExecutionException, InterruptedException {
        Optional<SubmissionDocument> submissionDocumentOptional = submissionService.findOne(id);
        SubmissionDocument submissionDocument;
        ProblemDocument problemDocument;
        if(submissionDocumentOptional.isPresent()) {
            submissionDocument = submissionDocumentOptional.get();
            problemDocument = problemService.findOne(submissionDocument.getProblemId()).get();
        }
        else {
            return;
        }
        double maxTime = 0;
        double maxMemory = 0;
        int rightTest = 0;
        File file = new File("Main.java");
        FileWriter writer = new FileWriter(file);
        writer.write(submissionDocument.getSource());
        writer.close();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, file.getPath());
        if(compilationResult != 0) {
            submissionDocument.setResult("COMPILATION ERROR");
            submissionService.update(submissionDocument);
            Map<String, String> map = new HashMap<>();
            map.put("result", "COMPILATION ERROR");
            messagingTemplate.convertAndSendToUser(
                    submissionDocument.getUserId(), "/queue/messages",
                    map
            );
            return;
        }
        Runtime runtime = Runtime.getRuntime();
        List<String> outputs = new ArrayList<>();
        for (int i = 0; i < problemDocument.getTestcase().size(); i++) {
            Map<String, String> test = problemDocument.getTestcase().get(i);

            Execute execute = utils.executeJudgeCode(test, Double.parseDouble(problemDocument.getTimeLimit()),runtime, "java");
            ResultTestCase resultTestCase = new ResultTestCase();
            if(execute.getOutput() != null) {
                if(execute.getResult() == null) {
                    File outputFile = new File(test.get("output"));
                    String outputTest = utils.handleTestResult(outputFile);

                    String temp = utils.checkResult(outputTest, execute.getOutput());

                    resultTestCase.setResult(temp);
                    resultTestCase.setTime(execute.getTime());
                    resultTestCase.setMemory(execute.getMemory());

                    outputs.add(temp);
                }
                else {
                    resultTestCase.setResult(execute.getResult());
                    resultTestCase.setTime(execute.getTime());
                    resultTestCase.setMemory(execute.getMemory());
                    outputs.add(execute.getResult());
                }
                maxTime = Math.max(maxTime, execute.getTime());
                maxMemory = Math.max(maxMemory, execute.getMemory());
            }
            else {
                resultTestCase.setResult(execute.getResult());
                outputs.add(execute.getResult());
                submissionDocument.setCompilerReport(execute.getCompilerReport());
            }

            submissionDocument.getTestcases().add(resultTestCase);
            messagingTemplate.convertAndSendToUser(
                    submissionDocument.getUserId(), "/queue/messages",
                    resultTestCase
            );
        }

        Map<String, String> map = new HashMap<>();
        String res = utils.checkFinalResult(outputs);
        if(res.equals("")) {
            submissionDocument.setResult("ACCEPTED");
            map.put("result", "ACCEPTED");
        }
        else {
            submissionDocument.setResult(res);
            map.put("result", res);
        }

        submissionDocument.setTime((float) maxTime);
        submissionDocument.setMemory((float) maxMemory);
        for(String output: outputs) {
            if(output.equals("ACCEPTED"))
                ++rightTest;
        }
        submissionDocument.setRightTest(rightTest);

        submissionService.update(submissionDocument);

        messagingTemplate.convertAndSendToUser(
                submissionDocument.getUserId(), "/queue/messages",
                map
        );

        file.delete();
    }

    @MessageMapping("/execute-python")
    public void execute(@Payload String id) throws IOException {
        Optional<SubmissionDocument> submissionDocumentOptional = submissionService.findOne(id);
        SubmissionDocument submissionDocument;
        ProblemDocument problemDocument;
        if(submissionDocumentOptional.isPresent()) {
            submissionDocument = submissionDocumentOptional.get();
            problemDocument = problemService.findOne(submissionDocument.getProblemId()).get();
        }
        else {
            return;
        }
        double maxTime = 0;
        double maxMemory = 0;
        int rightTest = 0;
        File file = new File("main.py");
        FileWriter writer = new FileWriter(file);
        writer.write(submissionDocument.getSource());
        writer.close();

        Runtime runtime = Runtime.getRuntime();
        List<String> outputs = new ArrayList<>();
        for (int i = 0; i < problemDocument.getTestcase().size(); i++) {
            Map<String, String> test = problemDocument.getTestcase().get(i);

            Execute execute = utils.executeJudgeCode(test, Double.parseDouble(problemDocument.getTimeLimit()),runtime, "python");
            ResultTestCase resultTestCase = new ResultTestCase();
            if(execute.getOutput() != null) {
                if(execute.getResult() == null) {
                    File outputFile = new File(test.get("output"));
                    String outputTest = utils.handleTestResult(outputFile);

                    String temp = utils.checkResult(outputTest, execute.getOutput());

                    resultTestCase.setResult(temp);
                    resultTestCase.setTime(execute.getTime());
                    resultTestCase.setMemory(execute.getMemory());

                    outputs.add(temp);
                }
                else {
                    resultTestCase.setResult(execute.getResult());
                    resultTestCase.setTime(execute.getTime());
                    resultTestCase.setMemory(execute.getMemory());
                    outputs.add(execute.getResult());
                }
                maxTime = Math.max(maxTime, execute.getTime());
                maxMemory = Math.max(maxMemory, execute.getMemory());
            }
            else {
                resultTestCase.setResult(execute.getResult());
                outputs.add(execute.getResult());
                submissionDocument.setCompilerReport(execute.getCompilerReport());
            }

            submissionDocument.getTestcases().add(resultTestCase);
            messagingTemplate.convertAndSendToUser(
                    submissionDocument.getUserId(), "/queue/messages",
                    resultTestCase
            );
        }

        Map<String, String> map = new HashMap<>();
        String res = utils.checkFinalResult(outputs);
        if(res.equals("")) {
            submissionDocument.setResult("ACCEPTED");
            map.put("result", "ACCEPTED");
        }
        else {
            submissionDocument.setResult(res);
            map.put("result", res);
        }

        submissionDocument.setTime((float) maxTime);
        submissionDocument.setMemory((float) maxMemory);
        for(String output: outputs) {
            if(output.equals("ACCEPTED"))
                ++rightTest;
        }
        submissionDocument.setRightTest(rightTest);

        submissionService.update(submissionDocument);

        messagingTemplate.convertAndSendToUser(
                submissionDocument.getUserId(), "/queue/messages",
                map
        );
        file.delete();


    }

    @MessageMapping("/submission")
    public void response(@Payload String id) {
        messagingTemplate.convertAndSendToUser(
                "public", "/queue/messages",
                submissionService.findOne(id)
        );
    }
}
