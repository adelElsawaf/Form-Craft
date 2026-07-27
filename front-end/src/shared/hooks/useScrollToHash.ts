import { useEffect } from 'react'
import { useLocation } from 'react-router-dom'
import { scrollToSection } from '@/shared/lib/scrollToSection'

export function useScrollToHash() {
  const { hash } = useLocation()

  useEffect(() => {
    if (!hash) {
      return
    }

    const sectionId = hash.replace('#', '')
    const frameId = window.requestAnimationFrame(() => {
      scrollToSection(sectionId)
    })

    return () => {
      window.cancelAnimationFrame(frameId)
    }
  }, [hash])
}
