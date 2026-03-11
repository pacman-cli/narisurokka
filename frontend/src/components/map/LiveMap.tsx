"use client"

import L from "leaflet"
import "leaflet/dist/leaflet.css"
import { useEffect } from "react"
import { MapContainer, Marker, Polyline, TileLayer, Tooltip, useMap } from "react-leaflet"

// Fix for Leaflet default icons in Next.js
const defaultIcon = L.icon({
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  iconRetinaUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41],
})

// Custom icons
const createCustomIcon = (color: string) => {
  return L.divIcon({
    className: "custom-div-icon",
    html: `<div style="background-color: ${color}; width: 24px; height: 24px; border-radius: 50%; border: 3px solid white; box-shadow: 0 4px 6px rgba(0,0,0,0.3);"></div>`,
    iconSize: [24, 24],
    iconAnchor: [12, 12],
  })
}

const icons = {
  user: createCustomIcon("#3b82f6"), // blue-500
  police: createCustomIcon("#2563eb"), // blue-600
  hospital: createCustomIcon("#ef4444"), // red-500
  safeZone: createCustomIcon("#10b981"), // emerald-500
}

export interface LocationPing {
  lat: number
  lng: number
  time?: string
  accuracy?: string
}

interface MapProps {
  currentLocation: { lat: number; lng: number }
  pingHistory: LocationPing[]
  activeFilters: string[] // "Police", "Hospitals", "Safe Zones"
}

// Map Updater component to recenter map when current location changes
function MapUpdater({ center }: { center: { lat: number; lng: number } }) {
  const map = useMap()
  useEffect(() => {
    map.flyTo([center.lat, center.lng], map.getZoom(), {
      animate: true,
      duration: 1.5,
    })
  }, [center, map])
  return null
}

export default function LiveMap({ currentLocation, pingHistory, activeFilters }: MapProps) {
  // Static POIs for demonstration
  const pois = [
    { type: "Police", lat: 23.815, lng: 90.418, name: "Gulshan Police Station" },
    { type: "Hospitals", lat: 23.805, lng: 90.410, name: "United Hospital" },
    { type: "Safe Zones", lat: 23.812, lng: 90.405, name: "Women's Safety Center (Gulshan)" },
    { type: "Safe Zones", lat: 23.818, lng: 90.420, name: "Baridhara DOHS Security Hub" },
  ]

  // Extract path coordinates for the polyline
  const pathPositions: [number, number][] = pingHistory.map((ping) => [ping.lat, ping.lng])

  return (
    <div className="h-[500px] w-full z-0 relative rounded-t-3xl overflow-hidden">
      <MapContainer
        center={[currentLocation.lat, currentLocation.lng]}
        zoom={15}
        scrollWheelZoom={true}
        className="h-full w-full"
        zoomControl={false} // We will add custom controls in the parent, or use default bottom-right if wanted
      >
        <MapUpdater center={currentLocation} />

        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png"
        />

        {/* Trail line */}
        {pathPositions.length > 1 && (
          <Polyline
            positions={pathPositions}
            color="#3b82f6"
            weight={4}
            opacity={0.6}
            dashArray="10, 10"
          />
        )}

        {/* User Marker */}
        <Marker position={[currentLocation.lat, currentLocation.lng]} icon={icons.user}>
          <Tooltip direction="top" offset={[0, -10]} opacity={1} permanent>
            <div className="font-bold text-slate-800 text-xs">Current Location</div>
          </Tooltip>
        </Marker>

        {/* POI Markers based on filters */}
        {pois
          .filter((poi) => activeFilters.includes(poi.type))
          .map((poi, idx) => {
            const iconObj =
              poi.type === "Police"
                ? icons.police
                : poi.type === "Hospitals"
                  ? icons.hospital
                  : icons.safeZone

            return (
              <Marker key={`${poi.type}-${idx}`} position={[poi.lat, poi.lng]} icon={iconObj}>
                <Tooltip direction="top" offset={[0, -10]}>
                  <div className="font-bold text-slate-800 text-xs">{poi.name}</div>
                </Tooltip>
              </Marker>
            )
          })}
      </MapContainer>
    </div>
  )
}
