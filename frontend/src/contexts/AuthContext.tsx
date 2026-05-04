"use client"

import { createContext, useContext, useEffect, useState, ReactNode } from "react"
import api, { ApiError } from "@/lib/api"

interface User {
  id: string
  fullName: string
  email: string
  phone?: string
}

interface AuthContextType {
  user: User | null
  isLoading: boolean
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  register: (data: { email: string; password: string; fullName?: string; phone?: string }) => Promise<void>
  logout: () => Promise<void>
  updateUser: (userData: Partial<User>) => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const initAuth = async () => {
      const token = localStorage.getItem("accessToken")
      const userData = localStorage.getItem("user")

      if (token && userData) {
        try {
          setUser(JSON.parse(userData))
        } catch {
          localStorage.removeItem("accessToken")
          localStorage.removeItem("refreshToken")
          localStorage.removeItem("user")
        }
      }
      setIsLoading(false)
    }

    initAuth()
  }, [])

  const login = async (email: string, password: string) => {
    const response = await api.auth.login({ email, password })
    localStorage.setItem("accessToken", response.accessToken)
    localStorage.setItem("refreshToken", response.refreshToken)

    const userData = { id: "", email, fullName: "" }
    setUser(userData)
    localStorage.setItem("user", JSON.stringify(userData))
  }

  const register = async (data: { email: string; password: string; fullName?: string; phone?: string }) => {
    const response = await api.auth.register(data)
    localStorage.setItem("accessToken", response.accessToken)
    localStorage.setItem("refreshToken", response.refreshToken)

    const userData = { id: "", email: data.email, fullName: data.fullName || "" }
    setUser(userData)
    localStorage.setItem("user", JSON.stringify(userData))
  }

  const logout = async () => {
    try {
      await api.auth.logout()
    } catch {
      // Ignore logout errors
    } finally {
      localStorage.removeItem("accessToken")
      localStorage.removeItem("refreshToken")
      localStorage.removeItem("user")
      setUser(null)
    }
  }

  const updateUser = (userData: Partial<User>) => {
    if (!user) return;
    const updated = { ...user, ...userData } as User;
    setUser(updated)
    localStorage.setItem("user", JSON.stringify(updated))
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoading,
        isAuthenticated: !!user,
        login,
        register,
        logout,
        updateUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}