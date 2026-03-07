package com.hallsymphony.service;

import com.hallsymphony.model.Issue;
import com.hallsymphony.util.DataStorage;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IssueService {
    private static final String ISSUE_FILE = "issues.txt";

    public static List<Issue> getAllIssues() {
        return DataStorage.readList(ISSUE_FILE).stream()
                .map(Issue::fromString)
                .filter(Objects::nonNull)  // Bug fix: null safety
                .collect(Collectors.toList());
    }

    public static List<Issue> getIssuesByCustomer(String customerId) {
        return getAllIssues().stream()
                .filter(i -> i.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    // Bug fix: max-ID based generation
    private static int getNextIssueId() {
        List<Issue> issues = getAllIssues();
        int maxId = 0;
        for (Issue i : issues) {
            try {
                int num = Integer.parseInt(i.getId().replace("ISSUE", ""));
                if (num > maxId) maxId = num;
            } catch (NumberFormatException ignored) {}
        }
        return maxId + 1;
    }

    public static boolean raiseIssue(String customerId, String bookingId, String description) {
        String id = "ISSUE" + getNextIssueId();
        Issue issue = new Issue(id, customerId, bookingId, description, "OPEN", null);
        DataStorage.appendToFile(ISSUE_FILE, issue);
        return true;
    }

    public static boolean updateIssueStatus(String issueId, String status, String assignedTo) {
        List<Issue> issues = getAllIssues();
        boolean found = false;
        for (Issue i : issues) {
            if (i.getId().equals(issueId)) {
                i.setStatus(status);
                if (assignedTo != null) i.setAssignedTo(assignedTo);
                found = true;
                break;
            }
        }
        if (found) {
            DataStorage.saveList(ISSUE_FILE, issues);
        }
        return found;
    }
}
