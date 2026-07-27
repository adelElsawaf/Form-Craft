import { Box, Button, Chip, Container, Grid, Stack, Typography } from '@mui/material'
import { Link as RouterLink } from 'react-router-dom'
import { FormBuilderMockup } from '@/pages/home/visuals/FormBuilderMockup'
import { ROUTES } from '@/shared/constants/routes'
import { FREE_FORM_LIMIT, HOME_SECTIONS, scrollToSection } from '@/shared/lib/scrollToSection'

const highlights = ['Drag & drop builder', 'Publish in minutes', `${FREE_FORM_LIMIT} forms free`]

export function HeroSection() {
  return (
    <Box
      component="section"
      sx={{
        position: 'relative',
        overflow: 'hidden',
        py: { xs: 6, md: 10 },
      }}
    >
      <Box
        aria-hidden
        sx={{
          position: 'absolute',
          inset: 0,
          background:
            'radial-gradient(circle at 15% 20%, rgba(79, 70, 229, 0.14), transparent 42%), radial-gradient(circle at 85% 10%, rgba(6, 182, 212, 0.12), transparent 38%)',
          pointerEvents: 'none',
        }}
      />

      <Container maxWidth="lg" sx={{ position: 'relative' }}>
        <Grid container spacing={{ xs: 4, md: 6 }} sx={{ alignItems: 'center' }}>
          <Grid size={{ xs: 12, md: 6 }}>
            <Stack
              spacing={3}
              sx={{
                textAlign: { xs: 'center', md: 'left' },
                alignItems: { xs: 'center', md: 'flex-start' },
              }}
            >
              <Typography
                variant="h2"
                sx={{
                  fontWeight: 800,
                  letterSpacing: '-0.03em',
                  fontSize: { xs: '2.25rem', sm: '3rem', md: '3.25rem' },
                  lineHeight: 1.1,
                  maxWidth: 500,
                }}
              >
                Build beautiful forms,{' '}
                <Box component="span" sx={{ color: 'primary.main' }}>
                  fast.
                </Box>
              </Typography>

              <Typography
                color="text.secondary"
                sx={{
                  maxWidth: 440,
                  fontSize: { xs: '1rem', md: '1.1rem' },
                  lineHeight: 1.7,
                }}
              >
                Design, publish, and collect responses with a drag-and-drop builder
                built for speed and clarity.
              </Typography>

              <Stack
                direction="row"
                spacing={1}
                sx={{
                  flexWrap: 'wrap',
                  justifyContent: { xs: 'center', md: 'flex-start' },
                  gap: 1,
                }}
              >
                {highlights.map((label) => (
                  <Chip
                    key={label}
                    label={label}
                    size="small"
                    sx={{
                      bgcolor: 'rgba(255,255,255,0.7)',
                      backdropFilter: 'blur(8px)',
                      border: '1px solid',
                      borderColor: 'divider',
                      fontWeight: 600,
                      color: 'text.secondary',
                    }}
                  />
                ))}
              </Stack>

              <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
                <Button
                  component={RouterLink}
                  to={ROUTES.auth.register}
                  variant="contained"
                  color="primary"
                  size="large"
                >
                  Get started
                </Button>
                <Button
                  variant="outlined"
                  color="primary"
                  size="large"
                  onClick={() => scrollToSection(HOME_SECTIONS.howItWorks)}
                  sx={{
                    borderColor: 'divider',
                    color: 'text.primary',
                    '&:hover': {
                      borderColor: 'primary.main',
                      bgcolor: 'rgba(79, 70, 229, 0.04)',
                    },
                  }}
                >
                  How it works
                </Button>
              </Stack>
            </Stack>
          </Grid>

          <Grid size={{ xs: 12, md: 6 }}>
            <FormBuilderMockup />
          </Grid>
        </Grid>
      </Container>
    </Box>
  )
}
