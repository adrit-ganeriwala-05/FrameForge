package com.project.webcrawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class MainController {

    @Autowired
    private WebCrawler webCrawler;

    @GetMapping("/recommendations")
    public String getRecommendations(@RequestParam int choice) {
        // Initialize the OperatingSystemMXBean to monitor CPU usage

        OperatingSystemMXBean bean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        // Print initial CPU usage
        CpuUsage(bean, "Initial CPU usage before starting web crawlers:");

        ArrayList<String> urls = new ArrayList<>();
        switch (choice) {
            case 1:
                urls.add("https://www.amazon.com/s?k=CPU+core+i5...");
                urls.add("https://www.walmart.com/search?q=CPU+core...");
                urls.add("https://www.microcenter.com/category/4294966995...");
                urls.add("https://www.amazon.com/s?k=GPU+GTX+1060...");
                urls.add("https://www.walmart.com/search?q=GPU+RTX+2060...");
                urls.add("https://www.microcenter.com/search/search_results...");
                break;
            case 2:
                urls.add("https://www.amazon.com/s?k=CPU+-+Core+i5...");
                urls.add("https://www.walmart.com/search?q=CPU+-+Core+i5...");
                urls.add("https://www.microcenter.com/search/search_results...");
                urls.add("https://www.amazon.com/s?k=GPU+GTX+960...");
                urls.add("https://www.walmart.com/search?q=GPU+GTX+1060...");
                urls.add("https://www.microcenter.com/search/search_results...");
                break;
            case 3:
                urls.add("https://www.amazon.com/s?k=CPU+core+i5...");
                urls.add("https://www.walmart.com/search?q=CPU+core+i5...");
                urls.add("https://www.microcenter.com/search/search_results...");
                urls.add("https://www.amazon.com/s?k=GPU+GTX+960...");
                urls.add("https://www.walmart.com/search?q=GPU+GTX+960...");
                urls.add("https://www.microcenter.com/search/search_results...");
                break;
            case 4:
                urls.add("https://www.amazon.com/s?k=CPU+core+i7...");
                urls.add("https://www.walmart.com/search?q=CPU+core+i7...");
                urls.add("https://www.microcenter.com/search/search_results...");
                urls.add("https://www.amazon.com/s?k=GPU+RTX+4070...");
                urls.add("https://www.walmart.com/search?q=GPU+RTX+4070...");
                urls.add("https://www.microcenter.com/search/search_results...");
                break;
            default:
                return "Invalid choice!";
        }

        webCrawler.clearData();

        // Printing the CPU usage before waiting for threads to finish
        CpuUsage(bean, "waiting for web crawlers to finish. CPU usage:");

        urls.forEach(url -> webCrawler.crawl(url, choice));

        // Print CPU usage after all threads have finished
        CpuUsage(bean, "web crawlers have finished. Current CPU Usage:");

        StringBuilder output = new StringBuilder();
        System.out.println(output.append(WebCrawler.getCheapestCPU()).append("\n"));
        System.out.println(output.append(WebCrawler.getCheapestGPU()).append("\n"));

        // Final CPU usage
        CpuUsage(bean, "Final CPU usage:");

        return WebCrawler.getCheapestCPU() + "\n" + WebCrawler.getCheapestGPU();
    }

    private static void CpuUsage(OperatingSystemMXBean bean, String str) {
        double cpuUsage = bean.getCpuLoad();
        if (cpuUsage >= 0) {
            System.out.printf("%s %.2f%%%n", str, cpuUsage * 100);
        } else {
            System.out.println(str + " information not available!!");
        }
    }
}
