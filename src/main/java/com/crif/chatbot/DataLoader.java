package com.crif.chatbot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.crif.chatbot.entity.Document;
import com.crif.chatbot.entity.Link;
import com.crif.chatbot.entity.Project;
import com.crif.chatbot.repository.DocumentRepository;
import com.crif.chatbot.repository.LinkRepository;
import com.crif.chatbot.repository.ProjectRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final LinkRepository linkRepo;
    private final ProjectRepository projectRepo;
    private final DocumentRepository docRepo;

    public DataLoader(LinkRepository linkRepo, ProjectRepository projectRepo, DocumentRepository docRepo) {
        this.linkRepo = linkRepo;
        this.projectRepo = projectRepo;
        this.docRepo = docRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        // ---------------------------
        // LINKS
        // ---------------------------
        if (linkRepo.count() == 0) {

            linkRepo.save(new Link(
                    "MyCRIF Portal",
                    "https://mycrif.crifnet.com/Pages/default.aspx",
                    "portal,mycrif,crif"
            ));

            linkRepo.save(new Link(
                    "JIRA Dashboard",
                    "https://jiraprd.crif.com/secure/Dashboard.jspa",
                    "jira,ticket,reg"
            ));

            System.out.println("✅ Links inserted.");
        }

        // ---------------------------
        // PROJECTS
        // ---------------------------
        if (projectRepo.count() == 0) {

            projectRepo.save(new Project(
                    "CJaaS",
                    "CJaaS is a CRIF project focused on Credit Journey as a Service.",
                    "cjaas,cj aas,credit journey"
            ));

            projectRepo.save(new Project(
                    "S1 India",
                    "S1 India is a CRIF project for scalable analytics and risk scoring workflows.",
                    "s1 india,s1,risk scoring"
            ));

            projectRepo.save(new Project(
                    "CFF",
                    "CFF is a CRIF product/project.",
                    "cff"
            ));

            projectRepo.save(new Project(
                    "Insight",
                    "Insight is a CRIF analytics and reporting initiative.",
                    "insight"
            ));

            System.out.println("✅ Projects inserted.");
        }

        // ---------------------------
        // DOCUMENTS (Projects + InfoSec + Tech Help)
        // ---------------------------
        if (docRepo.count() == 0) {

            // Project Documentation
            docRepo.save(new Document(
                    "CJaaS",
                    "CJaaS Overview Document",
                    "This is a dummy documentation entry for CJaaS. It contains overview, scope, and references.",
                    "cjaas,overview,doc,documentation"
            ));

            docRepo.save(new Document(
                    "S1 India",
                    "S1 India Architecture Notes",
                    "This is a dummy documentation entry for S1 India. It contains architecture notes and workflow summary.",
                    "s1 india,architecture,doc,documentation"
            ));

            // ---------------------------
            // InfoSec & Compliance (Dummy)
            // ---------------------------
            docRepo.save(new Document(
                    "InfoSec",
                    "Password Policy",
                    "Dummy InfoSec policy: Use strong passwords (12+ chars), avoid reuse, enable MFA, and rotate passwords periodically.",
                    "infosec,compliance,password policy,mfa,password"
            ));

            docRepo.save(new Document(
                    "InfoSec",
                    "VPN & Remote Access",
                    "Dummy guideline: Use approved VPN for remote access. Do not access CRIF resources from public/unsecured networks.",
                    "infosec,compliance,vpn,remote access"
            ));

            docRepo.save(new Document(
                    "InfoSec",
                    "Data Classification Guidelines",
                    "Dummy guideline: Classify information as Public/Internal/Confidential. Do not share Confidential data externally.",
                    "infosec,compliance,data classification,confidential"
            ));

            docRepo.save(new Document(
                    "InfoSec",
                    "Incident Reporting Procedure",
                    "Dummy procedure: If you suspect a security incident, immediately inform InfoSec team and raise an internal ticket. Do not delete evidence.",
                    "infosec,incident,security breach,report incident,compliance"
            ));

            // ------------------------------------
            // Common Technical / Functional Help
            // ------------------------------------
            docRepo.save(new Document(
                    "Tech Help",
                    "How to Raise an IT Ticket",
                    "Dummy steps: Go to ticketing portal (JIRA/Reg). Select category -> provide details -> attach screenshot -> submit ticket.",
                    "tech help,it ticket,raise ticket,jira,reg"
            ));

            docRepo.save(new Document(
                    "Tech Help",
                    "How to Request Access",
                    "Dummy steps: Raise access request ticket. Mention application name, business justification, manager approval, and role required.",
                    "tech help,access request,permissions,role request"
            ));

            docRepo.save(new Document(
                    "Tech Help",
                    "Password Reset Help",
                    "Dummy steps: Use password reset portal or raise ticket. Ensure MFA enrollment. Contact IT Helpdesk if locked out.",
                    "tech help,password reset,locked out,mfa"
            ));

            docRepo.save(new Document(
                    "Tech Help",
                    "Common Troubleshooting",
                    "Dummy steps: Check VPN connection, restart app, clear cache/browser, verify network. If issue persists, raise ticket with logs/screenshots.",
                    "tech help,troubleshoot,network,vpn,issue"
            ));

            System.out.println("✅ Documents inserted.");
        }

        System.out.println("✅ Dummy CRIF knowledge base is ready.");
    }
}
