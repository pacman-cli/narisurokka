import dynamic from "next/dynamic"

// Dynamically import the LiveMap component, disabling SSR.
// This is strictly required because Leaflet relies on the `window` object which doesn't exist during Next.js server rendering.
export const DynamicLiveMap = dynamic(() => import("./LiveMap"), {
    ssr: false,
    loading: () => (
        <div className= "h-[500px] w-full bg-slate-50 flex flex-col items-center justify-center text-slate-400 rounded-t-3xl border-b border-slate-200" >
        <div className="h-8 w-8 border-4 border-slate-200 border-t-primary rounded-full animate-spin mb-4" />
            < span className="text-sm font-medium" > Loading map tiles...</span>
            </div>
  ),
});
