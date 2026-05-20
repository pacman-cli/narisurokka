const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080"

class ApiError extends Error {
  constructor(public status: number, message: string) {
    super(message)
    this.name = "ApiError"
  }
}

async function fetchApi<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  const token = typeof window !== "undefined" ? localStorage.getItem("accessToken") : null

  const headers: HeadersInit = {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
    ...options.headers,
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  })

  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: "An error occurred" }))
    throw new ApiError(response.status, error.message || "An error occurred")
  }

  if (response.status === 204) {
    return undefined as T
  }

  return response.json()
}

export const api = {
  // Auth
  auth: {
    register: (data: { email: string; password: string; fullName?: string; phone?: string }) =>
      fetchApi<{ accessToken: string; refreshToken: string }>("/api/v1/auth/register", {
        method: "POST",
        body: JSON.stringify(data),
      }),

    login: (data: { email: string; password: string }) =>
      fetchApi<{ accessToken: string; refreshToken: string }>("/api/v1/auth/login", {
        method: "POST",
        body: JSON.stringify(data),
      }),

    refresh: (refreshToken: string) =>
      fetchApi<{ accessToken: string; refreshToken: string }>("/api/v1/auth/refresh", {
        method: "POST",
        body: JSON.stringify({ refreshToken }),
      }),

    logout: () =>
      fetchApi<void>("/api/v1/auth/logout", {
        method: "POST",
      }),
  },

  // Users
  users: {
    register: (data: {
      fullName: string
      email: string
      phone: string
      dateOfBirth?: string
      gender?: string
      bloodGroup?: string
      address?: string
    }) =>
      fetchApi<{ id: string }>("/api/v1/users/register", {
        method: "POST",
        body: JSON.stringify(data),
      }),

    getById: (id: string) =>
      fetchApi<{
        id: string
        fullName: string
        email: string
        phone: string
        dateOfBirth?: string
        gender?: string
        bloodGroup?: string
        address?: string
        isActive: boolean
      }>(`/api/v1/users/${id}`),

    update: (id: string, data: Partial<{
      fullName: string
      email: string
      phone: string
      dateOfBirth: string
      gender: string
      bloodGroup: string
      address: string
    }>) =>
      fetchApi<{ id: string }>(`/api/v1/users/${id}`, {
        method: "PUT",
        body: JSON.stringify(data),
      }),

    getPreferences: (userId: string) =>
      fetchApi<{
        id: string
        userId: string
        enableLiveTracking: boolean
        sosShakeTrigger: boolean
        notificationSms: boolean
        notificationEmail: boolean
        notificationPush: boolean
        language: string
      }>(`/api/v1/users/${userId}/preferences`),

    updatePreferences: (userId: string, data: {
      enableLiveTracking?: boolean
      sosShakeTrigger?: boolean
      notificationSms?: boolean
      notificationEmail?: boolean
      notificationPush?: boolean
      language?: string
    }) =>
      fetchApi<{ id: string }>(`/api/v1/users/${userId}/preferences`, {
        method: "PUT",
        body: JSON.stringify(data),
      }),

    getTrustedContacts: (userId: string) =>
      fetchApi<Array<{
        id: string
        userId: string
        contactName: string
        contactPhone: string
        contactEmail?: string
        relationship?: string
        isPrimary: boolean
        notifySms: boolean
        notifyEmail: boolean
        notifyWhatsapp: boolean
      }>>(`/api/v1/users/${userId}/contacts`),

    addTrustedContact: (userId: string, data: {
      contactName: string
      contactPhone: string
      contactEmail?: string
      relationship?: string
      isPrimary?: boolean
      notifySms?: boolean
      notifyEmail?: boolean
      notifyWhatsapp?: boolean
    }) =>
      fetchApi<{ id: string }>(`/api/v1/users/${userId}/contacts`, {
        method: "POST",
        body: JSON.stringify(data),
      }),

    deleteTrustedContact: (userId: string, contactId: string) =>
      fetchApi<void>(`/api/v1/users/${userId}/contacts/${contactId}`, {
        method: "DELETE",
      }),
  },

  // SOS
  sos: {
    trigger: (data: { userId: string; lat: number; lng: number }) =>
      fetchApi<{
        id: string
        userId: string
        status: string
        triggeredAt: string
        lat: number
        lng: number
      }>("/api/v1/sos/trigger", {
        method: "POST",
        body: JSON.stringify(data),
      }),

    cancel: (data: { userId: string; reason: string }) =>
      fetchApi<{
        id: string
        userId: string
        status: string
      }>("/api/v1/sos/cancel", {
        method: "POST",
        body: JSON.stringify(data),
      }),

    resolve: (data: { userId: string; resolutionNotes?: string }) =>
      fetchApi<{
        id: string
        userId: string
        status: string
      }>("/api/v1/sos/resolve", {
        method: "POST",
        body: JSON.stringify(data),
      }),

    getActive: (userId: string) =>
      fetchApi<{
        id: string
        userId: string
        status: string
        triggeredAt: string
        lat: number
        lng: number
      } | null>(`/api/v1/sos/active/${userId}`),
  },

  // Location
  location: {
    update: (data: { sosId: string; userId: string; lat: number; lng: number }) =>
      fetchApi<{
        id: string
        sosId: string
        userId: string
        lat: number
        lng: number
        timestamp: string
      }>("/api/v1/location/update", {
        method: "POST",
        body: JSON.stringify(data),
      }),

    getLatest: (userId: string) =>
      fetchApi<{
        lat: number
        lng: number
        timestamp: string
      }>(`/api/v1/location/latest/${userId}`),

    getHistory: (sosId: string) =>
      fetchApi<Array<{
        lat: number
        lng: number
        timestamp: string
      }>>(`/api/v1/location/history/${sosId}`),
  },

  // Notifications
  notifications: {
    getByUser: (userId: string) =>
      fetchApi<Array<{
        id: string
        userId: string
        sosId?: string
        type: string
        status: string
        subject: string
        message: string
        createdAt: string
      }>>(`/api/v1/notifications/user/${userId}`),

    getBySos: (sosId: string) =>
      fetchApi<Array<{
        id: string
        userId: string
        sosId: string
        type: string
        status: string
        subject: string
        message: string
        createdAt: string
      }>>(`/api/v1/notifications/sos/${sosId}`),
  },
}

export { ApiError }
export default api