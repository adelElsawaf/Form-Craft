import type { ReactNode } from 'react'
import { Box, Container, Stack, Typography } from '@mui/material'
import DynamicFormIcon from '@mui/icons-material/DynamicForm'
import CheckCircleOutlineOutlinedIcon from '@mui/icons-material/CheckCircleOutlineOutlined'
import { Link as RouterLink } from 'react-router-dom'
import { ROUTES } from '@/shared/constants/routes'

type AuthSplitLayoutProps = {
  brandTitle: string
  brandSubtitle: string
  perks: string[]
  mobileBadge?: string
  title: string
  subtitle: string
  reversed?: boolean
  children: ReactNode
}

function BrandPanel({
  brandTitle,
  brandSubtitle,
  perks,
  reversed = false,
}: Pick<AuthSplitLayoutProps, 'brandTitle' | 'brandSubtitle' | 'perks' | 'reversed'>) {
  return (
    <Box
      sx={{
        position: 'relative',
        display: { xs: 'none', md: 'flex' },
        flex: 1,
        flexDirection: 'column',
        justifyContent: 'center',
        px: { md: 6, lg: 10 },
        py: 8,
        overflow: 'hidden',
        bgcolor: 'primary.main',
        color: 'primary.contrastText',
      }}
    >
      <Box
        aria-hidden
        sx={{
          position: 'absolute',
          inset: 0,
          background: reversed
            ? 'radial-gradient(circle at 80% 80%, rgba(255,255,255,0.12), transparent 45%), radial-gradient(circle at 20% 20%, rgba(6, 182, 212, 0.35), transparent 40%)'
            : 'radial-gradient(circle at 20% 80%, rgba(255,255,255,0.12), transparent 45%), radial-gradient(circle at 80% 20%, rgba(6, 182, 212, 0.35), transparent 40%)',
          pointerEvents: 'none',
        }}
      />

      <Stack spacing={4} sx={{ position: 'relative', maxWidth: 420 }}>
        <Box
          component={RouterLink}
          to={ROUTES.home}
          sx={{
            display: 'inline-flex',
            alignItems: 'center',
            gap: 1.25,
            textDecoration: 'none',
            color: 'inherit',
            width: 'fit-content',
          }}
        >
          <Box
            sx={{
              width: 40,
              height: 40,
              borderRadius: 2,
              display: 'grid',
              placeItems: 'center',
              bgcolor: 'rgba(255,255,255,0.15)',
              backdropFilter: 'blur(8px)',
            }}
          >
            <DynamicFormIcon sx={{ fontSize: 24 }} />
          </Box>
          <Typography variant="h6" sx={{ fontWeight: 700, letterSpacing: '-0.02em' }}>
            FormCraft
          </Typography>
        </Box>

        <Stack spacing={1.5}>
          <Typography
            variant="h3"
            sx={{
              fontWeight: 800,
              letterSpacing: '-0.03em',
              lineHeight: 1.15,
              fontSize: { md: '2.25rem', lg: '2.75rem' },
            }}
          >
            {brandTitle}
          </Typography>
          <Typography sx={{ opacity: 0.85, lineHeight: 1.7, fontSize: '1.05rem' }}>
            {brandSubtitle}
          </Typography>
        </Stack>

        <Stack spacing={1.75}>
          {perks.map((perk) => (
            <Stack key={perk} direction="row" spacing={1.5} sx={{ alignItems: 'flex-start' }}>
              <CheckCircleOutlineOutlinedIcon sx={{ fontSize: 22, mt: 0.15, opacity: 0.9, flexShrink: 0 }} />
              <Typography sx={{ fontWeight: 500, lineHeight: 1.5 }}>{perk}</Typography>
            </Stack>
          ))}
        </Stack>
      </Stack>
    </Box>
  )
}

export function AuthSplitLayout({
  brandTitle,
  brandSubtitle,
  perks,
  mobileBadge,
  title,
  subtitle,
  reversed = false,
  children,
}: AuthSplitLayoutProps) {
  const formPanel = (
    <Box
      sx={{
        flex: 1,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        px: 2,
        py: { xs: 4, md: 6 },
      }}
    >
      <Container maxWidth="xs" disableGutters>
        <Stack spacing={3.5}>
          <Stack
            spacing={2}
            sx={{
              display: { xs: 'flex', md: 'none' },
              alignItems: 'center',
              textAlign: 'center',
            }}
          >
            <Box
              component={RouterLink}
              to={ROUTES.home}
              sx={{
                display: 'inline-flex',
                alignItems: 'center',
                gap: 1,
                textDecoration: 'none',
                color: 'inherit',
              }}
            >
              <Box
                sx={{
                  width: 36,
                  height: 36,
                  borderRadius: 2,
                  display: 'grid',
                  placeItems: 'center',
                  bgcolor: 'primary.main',
                  color: 'primary.contrastText',
                }}
              >
                <DynamicFormIcon sx={{ fontSize: 22 }} />
              </Box>
              <Typography variant="h6" sx={{ fontWeight: 700, letterSpacing: '-0.02em' }}>
                FormCraft
              </Typography>
            </Box>

            {mobileBadge && (
              <Box
                sx={{
                  py: 1.25,
                  px: 2,
                  borderRadius: 2,
                  bgcolor: 'rgba(79, 70, 229, 0.06)',
                  border: '1px dashed',
                  borderColor: 'primary.main',
                }}
              >
                <Typography variant="body2" sx={{ fontWeight: 700, color: 'primary.main' }}>
                  {mobileBadge}
                </Typography>
              </Box>
            )}
          </Stack>

          <Stack spacing={0.75}>
            <Typography
              variant="h4"
              sx={{
                fontWeight: 800,
                letterSpacing: '-0.02em',
                fontSize: { xs: '1.75rem', sm: '2rem' },
                textAlign: { xs: 'center', md: 'left' },
              }}
            >
              {title}
            </Typography>
            <Typography
              color="text.secondary"
              sx={{
                lineHeight: 1.6,
                textAlign: { xs: 'center', md: 'left' },
              }}
            >
              {subtitle}
            </Typography>
          </Stack>

          {children}
        </Stack>
      </Container>
    </Box>
  )

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        flexDirection: { xs: 'column', md: reversed ? 'row-reverse' : 'row' },
        bgcolor: 'background.default',
      }}
    >
      <BrandPanel
        brandTitle={brandTitle}
        brandSubtitle={brandSubtitle}
        perks={perks}
        reversed={reversed}
      />
      {formPanel}
    </Box>
  )
}
