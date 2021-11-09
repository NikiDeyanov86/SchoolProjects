package org.elsys.ip.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang.RandomStringUtils;

public class ChronoServlet extends HttpServlet {

    private class Laps {
        List<Instant> laps = new ArrayList<>();
    }

    private Map<String, Instant> chronometers = new HashMap<>();
    private Map<String, Laps> chronoId_laps = new HashMap<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> pathArgs = Arrays.stream(req.getPathInfo().split("/")).filter(x -> !x.equals("")).collect(Collectors.toList());

        if (pathArgs.size() != 2 || !pathArgs.get(0).equals("stopwatch") ) {
            resp.setStatus(400);
            return;
        }
        String command = pathArgs.get(1);
        String generatedId;
        if (command.equals("start")) {

            resp.setStatus(201);

            while (true) {
                generatedId = RandomStringUtils.random(3, true, true);
                if (chronometers.containsKey(generatedId)) {
                    continue;
                } else {
                    chronometers.put(generatedId, Instant.now());
                    chronoId_laps.put(generatedId, new Laps());

                    break;
                }
            }

            resp.getWriter().println(generatedId);
        } else {
            resp.setStatus(400);
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> pathArgs = Arrays.stream(req.getPathInfo().split("/")).filter(x -> !x.equals("")).collect(Collectors.toList());

        if (pathArgs.size() != 2  || !pathArgs.get(0).equals("stopwatch")) {
            resp.setStatus(400);
            return;
        }
        if(!chronometers.containsKey(pathArgs.get(1))) {
            resp.setStatus(404);
            return;
        }

        Duration time = Duration.between(chronometers.get(pathArgs.get(1)), Instant.now());

        if(time.toHoursPart() < 100) {
            String res = String.format("%02d:%02d:%02d", time.toHoursPart(), time.toMinutesPart(), time.toSecondsPart());
            resp.getWriter().println(res);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> pathArgs = Arrays.stream(req.getPathInfo().split("/")).filter(x -> !x.equals("")).collect(Collectors.toList());

        if (pathArgs.size() != 3 || !pathArgs.get(0).equals("stopwatch")) {
            resp.setStatus(400);
            return;
        }
        if(!chronometers.containsKey(pathArgs.get(1))) {
            resp.setStatus(404);
            return;
        }
        String command = pathArgs.get(2);
        if(command.equals("lap")) {
            resp.setStatus(200);

            String chronoId = pathArgs.get(1);
            chronoId_laps.get(chronoId).laps.add(Instant.now());

            if(chronoId_laps.get(chronoId).laps.size() == 1) {
                Duration time = Duration.between(chronometers.get(chronoId), chronoId_laps.get(chronoId).laps.get(0));

                if(time.toHoursPart() < 100) {
                    String res = String.format("%02d:%02d:%02d", time.toHoursPart(), time.toMinutesPart(), time.toSecondsPart());
                    resp.getWriter().println(res);
                }
            }
            else {
                Temporal start = chronoId_laps.get(chronoId).laps.get(chronoId_laps.get(chronoId).laps.size() - 2);
                Temporal end = chronoId_laps.get(chronoId).laps.get(chronoId_laps.get(chronoId).laps.size() - 1);
                Duration time = Duration.between(start, end);

                if(time.toHoursPart() < 100) {
                    String res = String.format("%02d:%02d:%02d", time.toHoursPart(), time.toMinutesPart(), time.toSecondsPart());
                    resp.getWriter().println(res);
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> pathArgs = Arrays.stream(req.getPathInfo().split("/")).filter(x -> !x.equals("")).collect(Collectors.toList());

        if (pathArgs.size() != 2 || !pathArgs.get(0).equals("stopwatch")) {
            resp.setStatus(400);
            return;
        }
        if(!chronometers.containsKey(pathArgs.get(1))) {
            resp.setStatus(404);
            return;
        }
        String chronoId = pathArgs.get(1);
        chronoId_laps.get(chronoId).laps.add(Instant.now());

        if(chronoId_laps.get(chronoId).laps.size() == 1) {
            Duration time = Duration.between(chronometers.get(chronoId), chronoId_laps.get(chronoId).laps.get(0));

            if(time.toHoursPart() < 100) {
                String res = String.format("%02d %02d:%02d:%02d / %02d:%02d:%02d", 1, time.toHoursPart(), time.toMinutesPart(), time.toSecondsPart(),
                        time.toHoursPart(), time.toMinutesPart(), time.toSecondsPart());

                resp.getWriter().println(res);
            }
        } else {
            for(int i = 1; i < chronoId_laps.get(chronoId).laps.size(); i++) {
                Duration lap = Duration.between(chronoId_laps.get(chronoId).laps.get(i - 1), chronoId_laps.get(chronoId).laps.get(i));
                Duration overall = Duration.between(chronometers.get(chronoId), chronoId_laps.get(chronoId).laps.get(i));

                if(lap.toHoursPart() < 100 && overall.toHoursPart() < 100) {
                    String res = String.format("%02d %02d:%02d:%02d / %02d:%02d:%02d", i, lap.toHoursPart(), lap.toMinutesPart(), lap.toSecondsPart(),
                            overall.toHoursPart(), overall.toMinutesPart(), overall.toSecondsPart());

                    resp.getWriter().println(res);
                }
            }
        }

        chronometers.remove(chronoId);
        chronoId_laps.remove(chronoId);
    }
}
