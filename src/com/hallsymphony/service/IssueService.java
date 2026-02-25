package com.hallsymphony.service;

import com.hallsymphony.model.Issue;
import com.hallsymphony.util.DataStorage;
import java.util.List;
import java.util.stream.Collectors;

public class IssueService {
    private static final String ISSUE_FILE = "issues.txt";

    public static List<Issue> getAllIssues() {
        return DataStorage.readList(ISSUE_FILE).stream()
                .map(Issue::fromString)
                .collect(Collectors.toList());
    }

    public static List<Issue> getIssuesByCustomer(String customerId) {
        return getAllIssues().stream()
                .filter(i -> i.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    public static boolean raiseIssue(String customerId, String bookingId, String description) {
        List<Issue> issues = getAllIssues();
        String id = "ISSUE" + (issues.size() + 1);
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
