import { Divider, Typography } from '@mui/material'

export function AuthDivider() {
  return (
    <Divider
      sx={{
        my: 0.5,
        '&::before, &::after': { borderColor: 'divider' },
      }}
    >
      <Typography variant="caption" color="text.secondary" sx={{ px: 1, letterSpacing: 0.4 }}>
        OR
      </Typography>
    </Divider>
  )
}
