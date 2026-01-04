package instrumers.backend.log.controller;

import instrumers.backend.log.dto.ExceptionLogDto;
import instrumers.backend.log.service.ExceptionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class ExceptionLogController {
    private final ExceptionLogService exceptionLogService;

    @GetMapping("/admin/logs")
    public String getLogs(@RequestParam(defaultValue = "0") int page, @ModelAttribute String errorMessage, Model model) {
        int pageSize = 20;
        int start = page * pageSize;
        int end = start + pageSize;

        try {
            List<ExceptionLogDto> exceptionLogDtos = exceptionLogService.getAllExceptionLogEntity(start, end);

            model.addAttribute("exceptionLogs", exceptionLogDtos);
            model.addAttribute("hasNext", exceptionLogDtos.size() == pageSize);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("exceptionLogs", List.of());
            model.addAttribute("hasNext", false);
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);

        return "log";
    }
}
