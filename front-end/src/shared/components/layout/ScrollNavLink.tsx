import { Box } from '@mui/material'
import { type MouseEvent } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { ROUTES } from '@/shared/constants/routes'
import {
  homeSectionHash,
  scrollToSection,
  type HomeSectionId,
} from '@/shared/lib/scrollToSection'

type ScrollNavLinkProps = {
  sectionId: HomeSectionId
  label: string
  onNavigate?: () => void
  sx?: Record<string, unknown>
}

export function ScrollNavLink({ sectionId, label, onNavigate, sx }: ScrollNavLinkProps) {
  const location = useLocation()
  const navigate = useNavigate()
  const hash = homeSectionHash(sectionId)
  const isActive = location.pathname === ROUTES.home && location.hash === hash

  const handleClick = (event: MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault()
    onNavigate?.()

    if (location.pathname === ROUTES.home) {
      scrollToSection(sectionId)
      navigate({ pathname: ROUTES.home, hash }, { replace: true })
      return
    }

    navigate({ pathname: ROUTES.home, hash })
  }

  return (
    <Box
      component="a"
      href={`${ROUTES.home}${hash}`}
      onClick={handleClick}
      sx={{
        color: isActive ? 'primary.main' : 'text.secondary',
        fontWeight: isActive ? 600 : 500,
        fontSize: '0.95rem',
        px: 1.5,
        py: 0.75,
        borderRadius: 2,
        textDecoration: 'none',
        cursor: 'pointer',
        transition: 'color 0.2s ease, background-color 0.2s ease',
        '&:hover': {
          color: 'primary.main',
          bgcolor: 'rgba(79, 70, 229, 0.06)',
        },
        ...sx,
      }}
    >
      {label}
    </Box>
  )
}
