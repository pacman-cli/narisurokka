"use client";

import React, { useState } from "react";
import IncidentForm from "@/components/incident/IncidentForm";
import IncidentHistory from "@/components/incident/IncidentHistory";

export default function ReportPage() {
  const [activeTab, setActiveTab] = useState<"new" | "history">("new");

  return (
    <div className="flex flex-col min-h-screen pt-4">
      <div className="flex flex-col gap-6">

        {/* Page Header */}
        <div>
          <h1 className="text-3xl font-black tracking-tight text-text-main">
            Incident Report
          </h1>
          <p className="text-text-muted mt-2 text-sm leading-relaxed">
            Quickly log suspicious activities or harassment securely. Your data is encrypted and immediately shared with your trusted contacts.
          </p>
        </div>

        {/* Tabs for fast swapping between input & history */}
        <div className="flex bg-bg-surface p-1 rounded-xl border border-border-subtle shadow-sm">
          <button
            onClick={() => setActiveTab("new")}
            className={`flex-1 py-2.5 text-sm font-bold rounded-lg transition-all ${
              activeTab === "new"
                ? "bg-bg-base text-text-main shadow-sm border border-border-subtle"
                : "text-text-muted hover:text-text-main hover:bg-neutral-50"
            }`}
            aria-pressed={activeTab === "new"}
            aria-label="New Report Tab"
          >
            New Report
          </button>
          <button
            onClick={() => setActiveTab("history")}
            className={`flex-1 py-2.5 text-sm font-bold rounded-lg transition-all ${
              activeTab === "history"
                ? "bg-bg-base text-text-main shadow-sm border border-border-subtle"
                : "text-text-muted hover:text-text-main hover:bg-neutral-50"
            }`}
            aria-pressed={activeTab === "history"}
            aria-label="History Tab"
          >
            History
          </button>
        </div>

        {/* Tab Content */}
        <div className="mt-2">
          {activeTab === "new" ? (
            <IncidentForm onSuccess={() => setActiveTab("history")} />
          ) : (
            <IncidentHistory />
          )}
        </div>
      </div>
    </div>
  );
}

