package com.project.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class WebCrawler {

    private static final int MAX_Depth = 3;
    private final ArrayList<String> visitedLinks = new ArrayList<>();

    private static final HashMap<String, ProductInfo> CPU_info = new HashMap<>();
    private static final HashMap<String, ProductInfo> GPU_info = new HashMap<>();

    public void crawl(String url, int id) {
        if (1 <= MAX_Depth && !visitedLinks.contains(url)) {
            Document doc = request(url);
            if (doc != null) {
                if (url.contains("CPU")) {
                    parseCPUs(doc);
                    parseCPUsWalmart(doc);
                    amazonCPUs(doc);
                } else if (url.contains("GPU")) {
                    parseGPUs(doc);
                    parseGPUsWalmart(doc);
                    amazonGPUs(doc);
                }
            }
            visitedLinks.add(url);
        }
    }

    private Document request(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36")
                    .get();
        } catch (IOException e) {
            return null;
        }
    }

    public void clearData() {
        CPU_info.clear();
        GPU_info.clear();
    }

    // microcenter CPUs data
    private void parseCPUs(Document doc) {
        Elements cpu_product = doc.select(".product_wrapper");

        for (Element product : cpu_product) {
            String name = product.select("a").attr("data-name");
            String priceText = product.select("span[itemprop=price]").text();

            String link = product.select("a").attr("href");
            double price = parse_prices(priceText);
            if (price >= 50) {
                String microcenter_url = "https://www.microcenter.com";
                String new_link = edit_link(microcenter_url, link);
                CPU_info.put(name, new ProductInfo(name, price, new_link));
                System.out.println("MicroCenter CPU: " + name);
                System.out.println("price: " + priceText);
                System.out.println("link: " + new_link);
            }
        }
    }

    // microcenter GPUs data
    private void parseGPUs(Document doc) {

        Elements gpu_product = doc.select(".product_wrapper");
        // using the Element data type. It is within the Elements
        for (Element product : gpu_product) {
            String name = product.select("a").attr("data-name");
            String priceText = product.select("span[itemprop=price]").text();
            ;
            String link = product.select("a").attr("href");
            double price = parse_prices(priceText);
            if (price >= 50) {
                String microcenter_url = "https://www.microcenter.com";
                String new_link = edit_link(microcenter_url, link);
                GPU_info.put(name, new ProductInfo(name, price, new_link));
                System.out.println("MicroCenter GPU: " + name);
                System.out.println("price: " + priceText);
                System.out.println("link: " + new_link);
            }

        }
    }

    private void parseCPUsWalmart(Document doc) {
        Elements cpu_products = doc.select("div[data-item-id]");

        for (Element product : cpu_products) {
            // Extract the product name
            Element nameElement = product.selectFirst("span.w_iUH7");
            String name = nameElement != null ? nameElement.text() : "N/A";
            String link = product.selectFirst("a").attr("href");

            // Extract the product price
            String priceWhole = product.select("div[data-automation-id=product-price] span.f2").text().replaceAll(
                    "[^\\d]",
                    "");
            String priceFraction = product.select("div[data-automation-id=product-price] span.f6").text()
                    .replaceAll("[^\\d]", "");
            String fullPrice = priceWhole.isEmpty() ? ""
                    : priceWhole + "." + (priceFraction.isEmpty() ? "00" : priceFraction);
            double price = parse_prices(fullPrice);
            if (price >= 50) {
                String walmart_url = "https://www.walmart.com";
                String new_link = edit_link(walmart_url, link);
                if (!name.isEmpty() && price > 0) {
                    CPU_info.put(name, new ProductInfo(name, price, new_link));

                    System.out.println("Walmart CPU: " + name);
                    System.out.println("price: $" + fullPrice);
                    System.out.println("link: " + new_link);

                }
            }
        }
    }

    private void parseGPUsWalmart(Document doc) {
        Elements gpu_products = doc.select("div[data-item-id]");

        for (Element product : gpu_products) {
            // Extracting the product name
            Element nameElement = product.selectFirst("span.w_iUH7");
            String name = nameElement != null ? nameElement.text() : "N/A";
            String link = product.selectFirst("a").attr("href");

            // Extracting the product price
            String priceWhole = product.select("div[data-automation-id=product-price] span.f2").text().replaceAll(
                    "[^\\d]",
                    "");
            String priceFraction = product.select("div[data-automation-id=product-price] span.f6").text()
                    .replaceAll("[^\\d]", "");
            String fullPrice = priceWhole.isEmpty() ? ""
                    : priceWhole + "." + (priceFraction.isEmpty() ? "00" : priceFraction);

            double price = parse_prices(fullPrice);
            if (price >= 50) {
                String walmart_url = "https://www.walmart.com";
                String new_link = edit_link(walmart_url, link);
                if (!name.isEmpty() && price > 0) {
                    GPU_info.put(name, new ProductInfo(name, price, new_link));

                    System.out.println("Walmart GPU: " + name);
                    System.out.println("price: $" + fullPrice);
                    System.out.println("link: " + new_link);

                }
            }
        }
    }

    private void amazonCPUs(Document doc) {
        Elements cpu_products = doc.select(".s-main-slot .s-result-item");

        for (Element product : cpu_products) {
            // Get the name and price of the CPU
            String name = product.select("span.a-text-normal").text();
            String priceWhole = product.select("span.a-price span.a-price-whole").text().replaceAll("[^\\d]", "");
            String priceFraction = product.select("span.a-price span.a-price-fraction").text().replaceAll("[^\\d]", "");
            String fullPrice = priceWhole.isEmpty() ? ""
                    : priceWhole + "." + (priceFraction.isEmpty() ? "00" : priceFraction);
            double price = parse_prices(fullPrice);
            String link = product.select("a").attr("href");
            if (price >= 50) {
                String amazon_url = "https://www.amazon.com";
                String new_link = edit_link(amazon_url, link);
                // Ensure both name and price are valid before saving
                if (!name.isEmpty() && price > 0) {
                    CPU_info.put(name, new ProductInfo(name, price, new_link));

                    System.out.println("Amazon CPU: " + name);
                    System.out.println("price: $" + fullPrice);
                    System.out.println("link: " + new_link);

                }
            }
        }
    }

    private void amazonGPUs(Document doc) {
        Elements gpu_products = doc.select(".s-main-slot .s-result-item");

        for (Element gpu_product : gpu_products) {
            // Get the name of the CPU
            String name = gpu_product.select("span.a-text-normal").text();

            // Get the price of the CPU
            String priceWhole = gpu_product.select("span.a-price span.a-price-whole").text().replaceAll("[^\\d]", "");
            String priceFraction = gpu_product.select("span.a-price span.a-price-fraction").text().replaceAll("[^\\d]",
                    "");
            String fullPrice = priceWhole.isEmpty() ? ""
                    : priceWhole + "." + (priceFraction.isEmpty() ? "00" : priceFraction);
            double price = parse_prices(fullPrice);
            String link = gpu_product.select("a").attr("href");
            if (price >= 50) {
                String amazon_url = "https://www.amazon.com";
                String new_link = edit_link(amazon_url, link);
                // Ensure both name and price are valid before saving
                if (!name.isEmpty() && price > 0) {
                    GPU_info.put(name, new ProductInfo(name, price, new_link));

                    System.out.println("Amazon GPU: " + name);
                    System.out.println("price: $" + fullPrice);
                    System.out.println("link: " + new_link);

                }
            }
        }
    }

    private String edit_link(String base, String link_extracted) {
        try {
            URI baseUri = new URI(base);
            URI resolvedUri = baseUri.resolve(link_extracted);
            return resolvedUri.toURL().toString();
        } catch (Exception e) {
            System.err.println("Error resolving link: " + e.getMessage());
            return link_extracted; // Return the original link if resolution fails
        }
    }

    // The parsePrice() method cleans price strings
    // (i.e. removing symbols and converting them to double data type)
    private double parse_prices(String final_price) throws NumberFormatException {
        if (final_price == null || final_price.isEmpty()) {
            return -1;
        }
        String price = final_price.replaceAll("[^\\d.]", "").replaceAll("\\.\\.", ".");
        return Double.parseDouble(price);
    }

    public static String getCheapestCPU() {
        return CPU_info.values().stream()
                .min((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                .map(details -> "Cheapest option for CPU: " + details)
                .orElse("Sorry!! No CPUs found!");
    }

    public static String getCheapestGPU() {
        return GPU_info.values().stream()
                .min((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                .map(details -> "Cheapest option for GPU: " + details)
                .orElse("Sorry!! No GPUs found!");
    }
}
