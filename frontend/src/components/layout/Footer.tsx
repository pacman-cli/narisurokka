import { Shield } from "lucide-react"
import Link from "next/link"

const footerLinks = {
  Services: [
    { label: "SOS Alerts", href: "/sos" },
    { label: "Live Tracking", href: "/tracking" },
    { label: "Notifications", href: "/notifications" },
  ],
  Platform: [
    { label: "API Documentation", href: "#" },
    { label: "System Status", href: "#" },
    { label: "Architecture", href: "#" },
  ],
  Legal: [
    { label: "Privacy Policy", href: "#" },
    { label: "Terms of Service", href: "#" },
    { label: "Contact", href: "#" },
  ],
}

export default function Footer() {
  return (
    <footer className="border-t border-slate-200 bg-white pt-16 pb-8">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-12 mb-12">
          {/* Brand */}
          <div>
            <Link href="/" className="flex items-center gap-2.5 mb-4">
              <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-primary text-white shadow-lg shadow-primary/25">
                <Shield className="h-5 w-5" />
              </div>
              <span className="text-lg font-bold text-slate-900">NariSurokkha</span>
            </Link>
            <p className="text-sm text-slate-500 leading-relaxed">
              Microservice-powered women safety platform with real-time SOS, location
              tracking, and instant notifications.
            </p>
          </div>

          {/* Links */}
          {Object.entries(footerLinks).map(([title, links]) => (
            <div key={title}>
              <h4 className="text-xs font-semibold text-slate-400 uppercase tracking-wider mb-4">
                {title}
              </h4>
              <ul className="space-y-3">
                {links.map((link) => (
                  <li key={link.label}>
                    <Link
                      href={link.href}
                      className="text-sm text-slate-600 hover:text-primary transition-colors"
                    >
                      {link.label}
                    </Link>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>

        {/* Bottom */}
        <div className="border-t border-slate-100 pt-8 flex flex-col sm:flex-row items-center justify-between gap-4">
          <p className="text-sm text-slate-400">
            © {new Date().getFullYear()} NariSurokkha. All rights reserved.
          </p>
          <div className="flex items-center gap-4">
            <span className="flex items-center gap-1.5 text-xs text-emerald-600 bg-emerald-50 rounded-full px-3 py-1 font-medium">
              <span className="h-1.5 w-1.5 rounded-full bg-emerald-500 animate-pulse" />
              All Systems Operational
            </span>
          </div>
        </div>
      </div>
    </footer>
  )
}
