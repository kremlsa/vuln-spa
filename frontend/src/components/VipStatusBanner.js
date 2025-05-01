// src/components/VipStatusBanner.js
import React from 'react';

function VipStatusBanner({ isVip }) {
  return isVip ? (
    <div className="vip-banner" style={{ color: 'gold', fontWeight: 'bold' }}>
      🎉 Вы VIP-пользователь!
    </div>
  ) : null;
}
export default VipStatusBanner;
