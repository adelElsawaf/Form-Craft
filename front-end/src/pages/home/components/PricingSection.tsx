import { Box, Button, Container, Grid, Stack, Typography } from '@mui/material'
import AccountBalanceWalletOutlinedIcon from '@mui/icons-material/AccountBalanceWalletOutlined'
import SubscriptionsOutlinedIcon from '@mui/icons-material/SubscriptionsOutlined'
import { Link as RouterLink } from 'react-router-dom'
import { SectionHeader } from '@/pages/home/components/SectionHeader'
import { ROUTES } from '@/shared/constants/routes'
import { FREE_FORM_LIMIT, HOME_SECTIONS } from '@/shared/lib/scrollToSection'

export function PricingSection() {
  return (
    <Box
      id={HOME_SECTIONS.pricing}
      component="section"
      sx={{
        py: { xs: 7, md: 9 },
        scrollMarginTop: { xs: 80, md: 88 },
        bgcolor: 'background.paper',
        borderTop: '1px solid',
        borderColor: 'divider',
      }}
    >
      <Container maxWidth="lg">
        <SectionHeader
          overline="Pricing"
          title={`${FREE_FORM_LIMIT} forms free, then choose how to pay`}
          description="Both options shown below — wallet or subscription, your choice after the free tier."
          align="center"
        />

        <Box
          sx={{
            mb: 3,
            py: 2,
            px: 3,
            borderRadius: 3,
            textAlign: 'center',
            bgcolor: 'rgba(79, 70, 229, 0.06)',
            border: '1px dashed',
            borderColor: 'primary.main',
          }}
        >
          <Typography variant="subtitle1" sx={{ fontWeight: 800, color: 'primary.main' }}>
            {FREE_FORM_LIMIT} forms included free — no card required
          </Typography>
        </Box>

        <Grid container spacing={3}>
          <Grid size={{ xs: 12, md: 6 }}>
            <Box
              sx={{
                p: 3.5,
                height: '100%',
                borderRadius: 4,
                bgcolor: '#0F172A',
                color: '#F8FAFC',
                display: 'flex',
                flexDirection: 'column',
              }}
            >
              <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center', mb: 2 }}>
                <AccountBalanceWalletOutlinedIcon />
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 800 }}>
                    Wallet
                  </Typography>
                  <Typography variant="caption" sx={{ color: '#94A3B8' }}>
                    Pay as you go
                  </Typography>
                </Box>
              </Stack>

              <Typography sx={{ color: '#CBD5E1', lineHeight: 1.7, mb: 2 }}>
                Top up credits when you need more. Pay only when you publish forms or collect
                responses.
              </Typography>

              <Box
                sx={{
                  p: 2,
                  mb: 2,
                  borderRadius: 2,
                  bgcolor: 'rgba(255,255,255,0.06)',
                  border: '1px solid rgba(255,255,255,0.1)',
                }}
              >
                <Typography variant="caption" sx={{ color: '#94A3B8' }}>
                  Credits never expire
                </Typography>
                <Typography variant="body2" sx={{ mt: 0.5 }}>
                  · Top up any amount
                </Typography>
                <Typography variant="body2">· Full usage history</Typography>
              </Box>

              <Button
                component={RouterLink}
                to={ROUTES.auth.register}
                variant="contained"
                sx={{ mt: 'auto', bgcolor: '#4F46E5', '&:hover': { bgcolor: '#4338CA' } }}
              >
                Choose wallet
              </Button>
            </Box>
          </Grid>

          <Grid size={{ xs: 12, md: 6 }}>
            <Box
              sx={{
                p: 3.5,
                height: '100%',
                borderRadius: 4,
                bgcolor: 'rgba(6, 182, 212, 0.08)',
                border: '2px solid',
                borderColor: 'secondary.main',
                display: 'flex',
                flexDirection: 'column',
              }}
            >
              <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center', mb: 2 }}>
                <SubscriptionsOutlinedIcon color="secondary" />
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 800 }}>
                    Subscription
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    Fixed monthly plan
                  </Typography>
                </Box>
              </Stack>

              <Typography variant="h3" sx={{ fontWeight: 800, color: 'secondary.dark', mb: 0.5 }}>
                $12
                <Typography component="span" variant="body1" color="text.secondary" sx={{ ml: 0.5 }}>
                  / month
                </Typography>
              </Typography>

              <Typography color="text.secondary" sx={{ lineHeight: 1.7, mb: 2 }}>
                One flat fee for unlimited forms. No credit tracking — predictable billing every
                month.
              </Typography>

              <Box sx={{ mb: 2 }}>
                <Typography variant="body2">✓ Unlimited forms</Typography>
                <Typography variant="body2">✓ Same price every month</Typography>
                <Typography variant="body2">✓ Cancel anytime</Typography>
              </Box>

              <Button
                component={RouterLink}
                to={ROUTES.auth.register}
                variant="contained"
                color="secondary"
                sx={{ mt: 'auto' }}
              >
                Choose subscription
              </Button>
            </Box>
          </Grid>
        </Grid>
      </Container>
    </Box>
  )
}
