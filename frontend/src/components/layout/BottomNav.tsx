import Link from "next/link";
import { usePathname } from "next/navigation";
import { FileEdit, Home, MapPin, Settings, User } from "lucide-react";

export const BottomNav = () => {
  const pathname = usePathname() || "";

  return (
    <nav className="sticky bottom-0 z-40 w-full bg-bg-surface/80 backdrop-blur-xl border-t border-border-subtle pb-safe px-2 shadow-[0_-4px_24px_rgba(0,0,0,0.02)]">
      <div className="flex items-center justify-around py-2 max-w-md mx-auto">
        <NavItem href="/" icon={<Home className="w-5 h-5 focus:outline-none" />} label="Home" active={pathname === "/"} />
        <NavItem href="/report" icon={<FileEdit className="w-5 h-5" />} label="Report" active={pathname === "/report"} />

        {/* Placeholder for SOS center button */}
        <div className="w-16 h-12" aria-hidden="true" />

        <NavItem href="/tracking" icon={<MapPin className="w-5 h-5" />} label="Tracking" active={pathname === "/tracking"} />
        <NavItem href="/settings" icon={<Settings className="w-5 h-5" />} label="Settings" active={pathname === "/settings"} />
      </div>
    </nav>
  );
};

function NavItem({ href, icon, label, active }: { href: string; icon: React.ReactNode; label: string; active: boolean }) {
  return (
    <Link href={href} className="flex flex-col items-center justify-center p-2 rounded-xl group relative focus:outline-none" prefetch={true}>
      <div className={`transition-all duration-200 ${active ? "text-brand-600 transform scale-110" : "text-text-muted group-hover:text-text-main group-hover:scale-105"}`}>
        {icon}
      </div>
      <span className={`text-[10px] mt-1.5 font-medium transition-colors duration-200 tracking-wide ${active ? "text-brand-600 font-semibold" : "text-text-muted"}`}>
        {label}
      </span>
      {/* Keyboard Focus state */}
      <span className="sr-only">{label} page</span>
      <div className="sr-only group-focus-visible:not-sr-only absolute inset-0 ring-2 ring-brand-500 rounded-xl pointer-events-none" />
    </Link>
  );
}
