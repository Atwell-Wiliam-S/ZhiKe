import { useState } from 'react';

// 模拟用户数据
const mockUsers = [
  { id: 1, username: 'admin', email: 'admin@example.com', role: '管理员', status: '活跃', registerTime: '2024-01-01 10:00:00' },
  { id: 2, username: 'user1', email: 'user1@example.com', role: '普通用户', status: '活跃', registerTime: '2024-01-02 11:30:00' },
  { id: 3, username: 'user2', email: 'user2@example.com', role: '普通用户', status: '禁用', registerTime: '2024-01-03 09:15:00' },
  { id: 4, username: 'editor', email: 'editor@example.com', role: '编辑', status: '活跃', registerTime: '2024-01-04 14:20:00' },
  { id: 5, username: 'user3', email: 'user3@example.com', role: '普通用户', status: '活跃', registerTime: '2024-01-05 16:45:00' },
  { id: 6, username: 'user4', email: 'user4@example.com', role: '普通用户', status: '禁用', registerTime: '2024-01-06 08:30:00' },
  { id: 7, username: 'moderator', email: 'moderator@example.com', role: '版主', status: '活跃', registerTime: '2024-01-07 13:10:00' },
  { id: 8, username: 'user5', email: 'user5@example.com', role: '普通用户', status: '活跃', registerTime: '2024-01-08 15:50:00' },
  { id: 9, username: 'user6', email: 'user6@example.com', role: '普通用户', status: '活跃', registerTime: '2024-01-09 10:25:00' },
  { id: 10, username: 'user7', email: 'user7@example.com', role: '普通用户', status: '禁用', registerTime: '2024-01-10 11:40:00' },
  { id: 11, username: 'user8', email: 'user8@example.com', role: '普通用户', status: '活跃', registerTime: '2024-01-11 09:55:00' },
  { id: 12, username: 'user9', email: 'user9@example.com', role: '普通用户', status: '活跃', registerTime: '2024-01-12 14:15:00' },
];

// 角色选项
const roleOptions = ['全部', '管理员', '编辑', '版主', '普通用户'];

// 状态选项
const statusOptions = ['全部', '活跃', '禁用'];

function App() {
  // 状态管理
  const [users, setUsers] = useState(mockUsers);
  const [filteredUsers, setFilteredUsers] = useState(mockUsers);
  const [roleFilter, setRoleFilter] = useState('全部');
  const [statusFilter, setStatusFilter] = useState('全部');
  const [searchTerm, setSearchTerm] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);

  // 过滤用户数据
  const filterUsers = () => {
    let filtered = users;

    // 角色筛选
    if (roleFilter !== '全部') {
      filtered = filtered.filter(user => user.role === roleFilter);
    }

    // 状态筛选
    if (statusFilter !== '全部') {
      filtered = filtered.filter(user => user.status === statusFilter);
    }

    // 搜索筛选
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      filtered = filtered.filter(user => 
        user.username.toLowerCase().includes(term) || 
        user.email.toLowerCase().includes(term)
      );
    }

    setFilteredUsers(filtered);
    setCurrentPage(1); // 重置到第一页
  };

  // 当筛选条件变化时触发过滤
  const handleFilterChange = () => {
    filterUsers();
  };

  // 当搜索词变化时触发过滤
  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    filterUsers();
  };

  // 计算分页信息
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentUsers = filteredUsers.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(filteredUsers.length / itemsPerPage);

  // 分页处理
  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  // 生成页码按钮
  const renderPagination = () => {
    const pageNumbers = [];
    for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(i);
    }

    return (
      <div className="flex justify-center mt-6">
        <nav className="flex items-center space-x-1">
          <button
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 1}
            className="px-3 py-1 rounded-md border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            上一页
          </button>
          {pageNumbers.map(number => (
            <button
              key={number}
              onClick={() => handlePageChange(number)}
              className={`px-3 py-1 rounded-md text-sm font-medium ${currentPage === number ? 'bg-primary text-white' : 'border border-gray-300 bg-white text-gray-700 hover:bg-gray-50'}`}
            >
              {number}
            </button>
          ))}
          <button
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === totalPages}
            className="px-3 py-1 rounded-md border border-gray-300 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            下一页
          </button>
        </nav>
      </div>
    );
  };

  // 渲染操作按钮
  const renderActionButtons = (user) => {
    return (
      <div className="flex space-x-2">
        <button className="px-2 py-1 text-xs bg-blue-500 text-white rounded hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400">
          编辑
        </button>
        <button className="px-2 py-1 text-xs bg-green-500 text-white rounded hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-400">
          {user.status === '活跃' ? '禁用' : '启用'}
        </button>
        <button className="px-2 py-1 text-xs bg-red-500 text-white rounded hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400">
          删除
        </button>
      </div>
    );
  };

  // 渲染状态标签
  const renderStatusBadge = (status) => {
    const isActive = status === '活跃';
    return (
      <span className={`px-2 py-1 text-xs rounded-full ${isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
        {status}
      </span>
    );
  };

  // 渲染角色标签
  const renderRoleBadge = (role) => {
    let colorClass = 'bg-gray-100 text-gray-800';
    switch (role) {
      case '管理员':
        colorClass = 'bg-purple-100 text-purple-800';
        break;
      case '编辑':
        colorClass = 'bg-blue-100 text-blue-800';
        break;
      case '版主':
        colorClass = 'bg-yellow-100 text-yellow-800';
        break;
      default:
        colorClass = 'bg-gray-100 text-gray-800';
    }
    return (
      <span className={`px-2 py-1 text-xs rounded-full ${colorClass}`}>
        {role}
      </span>
    );
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* 顶部导航栏 */}
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <h1 className="text-xl font-semibold text-gray-900">用户管理</h1>
        </div>
      </header>

      {/* 主内容区 */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        {/* 顶部操作栏 */}
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-lg font-medium text-gray-900">用户列表</h2>
          <button className="px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400">
            添加用户
          </button>
        </div>

        {/* 筛选栏 */}
        <div className="bg-white p-4 rounded-lg shadow-sm mb-6">
          <form onSubmit={handleSearchSubmit} className="flex flex-wrap gap-4">
            {/* 角色筛选 */}
            <div className="flex items-center space-x-2">
              <label htmlFor="role" className="text-sm font-medium text-gray-700">角色:</label>
              <select
                id="role"
                value={roleFilter}
                onChange={(e) => {
                  setRoleFilter(e.target.value);
                  handleFilterChange();
                }}
                className="px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary"
              >
                {roleOptions.map(role => (
                  <option key={role} value={role}>{role}</option>
                ))}
              </select>
            </div>

            {/* 搜索框 */}
            <div className="flex-1 min-w-[200px]">
              <div className="relative">
                <input
                  type="text"
                  placeholder="搜索用户名或邮箱"
                  value={searchTerm}
                  onChange={handleSearchChange}
                  className="w-full px-3 py-2 pl-10 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary"
                />
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <svg className="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                  </svg>
                </div>
              </div>
            </div>

            {/* 状态筛选 */}
            <div className="flex items-center space-x-2">
              <label htmlFor="status" className="text-sm font-medium text-gray-700">状态:</label>
              <select
                id="status"
                value={statusFilter}
                onChange={(e) => {
                  setStatusFilter(e.target.value);
                  handleFilterChange();
                }}
                className="px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary focus:border-primary"
              >
                {statusOptions.map(status => (
                  <option key={status} value={status}>{status}</option>
                ))}
              </select>
            </div>

            {/* 搜索按钮 */}
            <button
              type="submit"
              className="px-4 py-2 bg-primary text-white rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400"
            >
              搜索
            </button>
          </form>
        </div>

        {/* 数据表格 - 桌面版 */}
        <div className="hidden md:block">
          <div className="bg-white shadow-sm rounded-lg overflow-hidden">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    用户名
                  </th>
                  <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    邮箱
                  </th>
                  <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    角色
                  </th>
                  <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    状态
                  </th>
                  <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    注册时间
                  </th>
                  <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    操作
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {currentUsers.length > 0 ? (
                  currentUsers.map(user => (
                    <tr key={user.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {user.username}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {user.email}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {renderRoleBadge(user.role)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {renderStatusBadge(user.status)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {user.registerTime}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        {renderActionButtons(user)}
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={6} className="px-6 py-10 text-center text-sm text-gray-500">
                      没有找到匹配的用户
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* 移动端卡片模式 */}
        <div className="md:hidden space-y-4">
          {currentUsers.length > 0 ? (
            currentUsers.map(user => (
              <div key={user.id} className="border rounded-lg p-4 shadow-sm bg-white">
                <div className="flex justify-between items-start mb-3">
                  <div>
                    <h3 className="font-medium text-gray-900">{user.username}</h3>
                    <p className="text-sm text-gray-500">{user.email}</p>
                  </div>
                  {renderStatusBadge(user.status)}
                </div>
                <div className="flex flex-wrap gap-2 mb-3">
                  {renderRoleBadge(user.role)}
                  <span className="px-2 py-1 text-xs bg-gray-100 text-gray-800 rounded-full">
                    {user.registerTime}
                  </span>
                </div>
                <div className="flex space-x-2">
                  {renderActionButtons(user)}
                </div>
              </div>
            ))
          ) : (
            <div className="bg-white p-6 rounded-lg shadow-sm text-center text-gray-500">
              没有找到匹配的用户
            </div>
          )}
        </div>

        {/* 分页 */}
        {totalPages > 1 && renderPagination()}
      </main>
    </div>
  );
}

export default App;
