// src/main/java/com/nexsys/util/NetworkUtils.java
package com.nexsys.util;

import java.net.*;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Network utilities for IP address and URL operations.
 */
public final class NetworkUtils {
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$"
    );

    private NetworkUtils() {
        // Utility class
    }

    /**
     * Check if an address is a loopback address.
     *
     * @param address the IP address string
     * @return true if loopback
     */
    public static boolean isLoopback(String address) {
        try {
            InetAddress addr = InetAddress.getByName(address);
            return addr.isLoopbackAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * Check if an address is on a private network.
     *
     * @param address the IP address string
     * @return true if private
     */
    public static boolean isPrivate(String address) {
        try {
            InetAddress addr = InetAddress.getByName(address);
            return addr.isSiteLocalAddress() || isInRange(addr, "10.0.0.0/8") ||
                    isInRange(addr, "172.16.0.0/12") || isInRange(addr, "192.168.0.0/16") ||
                    isInRange(addr, "fd00::/8");
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * Check if an address is local (loopback, private, or link-local).
     *
     * @param address the IP address string
     * @return true if local
     */
    public static boolean isLocal(String address) {
        try {
            InetAddress addr = InetAddress.getByName(address);
            return addr.isLoopbackAddress() || addr.isSiteLocalAddress() ||
                    addr.isLinkLocalAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * Check if a string is a valid IP address.
     *
     * @param address the address string
     * @return true if valid IP address
     */
    public static boolean isIpAddress(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }

        try {
            InetAddress.getByName(address);
            // Additional check to ensure it's not a hostname
            return IPV4_PATTERN.matcher(address).matches() || address.contains(":");
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * Check if a string is a valid IPv4 address.
     *
     * @param address the address string
     * @return true if valid IPv4 address
     */
    public static boolean isIpv4Address(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }

        try {
            InetAddress addr = InetAddress.getByName(address);
            return addr instanceof Inet4Address && IPV4_PATTERN.matcher(address).matches();
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * Check if a string is a valid IPv6 address.
     *
     * @param address the address string
     * @return true if valid IPv6 address
     */
    public static boolean isIpv6Address(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }

        try {
            InetAddress addr = InetAddress.getByName(address);
            return addr instanceof Inet6Address;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * Normalize a URL by removing default ports and trailing slashes.
     *
     * @param url the URL string
     * @return normalized URL
     * @throws IllegalArgumentException if URL is invalid
     */
    public static String normalizeUrl(String url) {
        Objects.requireNonNull(url, "URL cannot be null");

        try {
            URI uri = new URI(url.trim());

            // Handle path - remove trailing slash except for root
            String path = uri.getPath();
            if (path == null || path.isEmpty()) {
                path = null;  // No path component
            } else if (path.equals("/")) {
                path = null;  // Root path should be omitted in normalized form
            } else if (path.endsWith("/") && path.length() > 1) {
                path = path.substring(0, path.length() - 1);
            }

            // Check if using default port
            int port = uri.getPort();
            String scheme = uri.getScheme();
            boolean isDefaultPort = (port == -1) ||
                    (scheme != null && scheme.equals("http") && port == 80) ||
                    (scheme != null && scheme.equals("https") && port == 443);

            // Rebuild URL
            URI normalized = new URI(
                    uri.getScheme(),
                    uri.getUserInfo(),
                    uri.getHost(),
                    isDefaultPort ? -1 : port,
                    path,
                    uri.getQuery(),
                    uri.getFragment()
            );

            return normalized.toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: " + url, e);
        }
    }

    /**
     * Check if an IP address is in a CIDR range.
     *
     * @param address the address to check
     * @param cidr the CIDR range
     * @return true if in range
     */
    private static boolean isInRange(InetAddress address, String cidr) {
        try {
            String[] parts = cidr.split("/");
            InetAddress subnet = InetAddress.getByName(parts[0]);
            int prefixLength = Integer.parseInt(parts[1]);

            byte[] addrBytes = address.getAddress();
            byte[] subnetBytes = subnet.getAddress();

            if (addrBytes.length != subnetBytes.length) {
                return false;
            }

            int bytesToCheck = prefixLength / 8;
            int bitsToCheck = prefixLength % 8;

            for (int i = 0; i < bytesToCheck; i++) {
                if (addrBytes[i] != subnetBytes[i]) {
                    return false;
                }
            }

            if (bitsToCheck > 0 && bytesToCheck < addrBytes.length) {
                int mask = 0xFF << (8 - bitsToCheck);
                return (addrBytes[bytesToCheck] & mask) == (subnetBytes[bytesToCheck] & mask);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}