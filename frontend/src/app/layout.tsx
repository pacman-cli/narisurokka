import QueryProvider from "@/components/providers/QueryProvider"
import type { Metadata } from "next"
import { Inter } from "next/font/google"
import "./globals.css"

const inter = Inter({
  subsets: ["latin"],
  variable: "--font-inter",
})

export const metadata: Metadata = {
  title: "NariSurokkha – Women Safety Platform",
  description:
    "Real-time SOS alerts, live location tracking, and instant notifications. Empowering women with technology-driven safety.",
  keywords: [
    "women safety",
    "SOS alert",
    "live tracking",
    "emergency notification",
    "NariSurokkha",
  ],
}

export default function RootLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="en" className={inter.variable}>
      <body className="font-sans antialiased">
        <QueryProvider>{children}</QueryProvider>
      </body>
    </html>
  )
}
