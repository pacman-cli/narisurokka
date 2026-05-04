"use client";

import React from "react";
import { motion } from "framer-motion";
import { Copy, MapPin, Clock, Search, ExternalLink } from "lucide-react";

// Mock Incident Interface
interface MockIncident {
  id: string;
  date: string;
  type: string;
  status: "Investigating" | "Resolved" | "Received";
  location: string;
}

const mockHistory: MockIncident[] = [
  {
    id: "INC-2024-04-18-B",
    date: "18 Apr 2024, 21:05",
    type: "Suspicious Activity",
    status: "Investigating",
    location: "Uttara, Sector 4",
  },
  {
    id: "INC-2024-03-02-A",
    date: "02 Mar 2024, 18:30",
    type: "Being Followed",
    status: "Resolved",
    location: "Dhanmondi, Road 27",
  },
];

export default function IncidentHistory() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="flex flex-col gap-4"
    >
      <div className="flex items-center justify-between mb-2 pb-2 border-b border-border-subtle">
        <h2 className="text-xl font-bold text-text-main tracking-tight">Recent Reports</h2>
        <button
          className="p-2 text-brand-600 hover:bg-brand-50 rounded-full transition-colors"
          aria-label="Search History"
        >
          <Search className="w-5 h-5" />
        </button>
      </div>

      {mockHistory.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-12 text-text-muted">
          <p className="text-sm font-medium">No incident history found.</p>
        </div>
      ) : (
        <div className="flex flex-col gap-3">
          {mockHistory.map((incident) => (
            <div
              key={incident.id}
              className="card p-4 hover:border-brand-300 transition-colors cursor-pointer group"
            >
              <div className="flex items-center justify-between mb-3">
                <span className="text-xs font-mono font-bold text-text-muted tracking-wider group-hover:text-brand-600 transition-colors">
                  {incident.id}
                </span>

                {/* Status Badge */}
                <span className={`text-[10px] font-bold px-2 py-1 rounded-full ${
                  incident.status === 'Resolved' 
                    ? 'bg-success-50 text-success-700 border border-success-200' 
                    : 'bg-warning-50 text-warning-700 border border-warning-200'
                }`}>
                  {incident.status.toUpperCase()}
                </span>
              </div>

              <h3 className="text-base font-bold text-text-main mb-3">{incident.type}</h3>

              <div className="flex flex-col gap-2">
                <div className="flex items-center gap-2 text-sm text-text-muted">
                  <MapPin className="w-4 h-4 text-brand-500 shrink-0" />
                  <span className="truncate">{incident.location}</span>
                </div>
                <div className="flex items-center justify-between mt-1">
                  <div className="flex items-center gap-2 text-xs text-text-muted">
                    <Clock className="w-4 h-4 shrink-0" />
                    <span>{incident.date}</span>
                  </div>
                  <ExternalLink className="w-4 h-4 text-brand-600 opacity-0 group-hover:opacity-100 transition-opacity" />
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </motion.div>
  );
}

